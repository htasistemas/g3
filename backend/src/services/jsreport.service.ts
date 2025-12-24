import fs from 'fs';
import path from 'path';

function ensureDependencyInstalled(packageName: string): void {
  try {
    require.resolve(packageName);
  } catch (error) {
    const typedError = error as NodeJS.ErrnoException;

    if (typedError.code === 'MODULE_NOT_FOUND' && typedError.message?.includes(`'${packageName}'`)) {
      const modulePath = path.join(process.cwd(), 'node_modules', packageName);

      if (!fs.existsSync(modulePath)) {
        throw new Error(
          `Dependência ausente: ${packageName}. Execute \"npm install\" em backend/ para instalar todas as dependências antes de iniciar o servidor.`
        );
      }
    }

    throw typedError;
  }
}

ensureDependencyInstalled('jsreport-core');
ensureDependencyInstalled('jsreport-chrome-pdf');
ensureDependencyInstalled('jsreport-handlebars');
ensureDependencyInstalled('puppeteer-core');
ensureDependencyInstalled('@sparticuz/chromium');

// Importações após a validação para evitar erros de inicialização por dependências não instaladas
const jsreport = require('jsreport-core') as typeof import('jsreport-core');
const jsreportChrome = require('jsreport-chrome-pdf') as typeof import('jsreport-chrome-pdf');
const jsreportHandlebars = require('jsreport-handlebars') as typeof import('jsreport-handlebars');

const instance = jsreport();

let initialized: Promise<void> | null = null;

async function initJsReport(): Promise<void> {
  if (initialized) return initialized;

  initialized = (async () => {
    const chromium = require('@sparticuz/chromium') as typeof import('@sparticuz/chromium');
    const puppeteer = require('puppeteer-core') as typeof import('puppeteer-core');
    const executablePath = await chromium.executablePath();

    if (!executablePath) {
      throw new Error('Não foi possível localizar o executável do Chromium para gerar PDFs.');
    }

    instance.use(
      jsreportChrome({
        launchOptions: {
          args: chromium.args,
          executablePath,
          headless: chromium.headless,
          ignoreHTTPSErrors: true,
          defaultViewport: { width: 1280, height: 720 }
        },
        puppeteerInstance: puppeteer
      })
    );

    instance.use(jsreportHandlebars());

    await instance.init();
  })();

  return initialized;
}

export async function renderPdf(template: {
  content: string;
  data?: Record<string, unknown>;
}): Promise<Buffer> {
  await initJsReport();

  const report = await instance.render({
    template: {
      content: template.content,
      engine: 'handlebars',
      recipe: 'chrome-pdf',
      chrome: {
        format: 'A4',
        marginTop: '12mm',
        marginBottom: '12mm',
        marginLeft: '12mm',
        marginRight: '12mm',
        printBackground: true,
        preferCssPageSize: true
      }
    },
    data: template.data ?? {}
  });

  return report.content as Buffer;
}
