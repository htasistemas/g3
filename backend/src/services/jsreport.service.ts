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

// Importações após a validação para evitar erros de inicialização por dependências não instaladas
const jsreport = require('jsreport-core') as typeof import('jsreport-core');
const jsreportChrome = require('jsreport-chrome-pdf') as typeof import('jsreport-chrome-pdf');
const jsreportHandlebars = require('jsreport-handlebars') as typeof import('jsreport-handlebars');

const instance = jsreport();
instance.use(jsreportChrome());
instance.use(jsreportHandlebars());

const initialized = instance.init();

export async function renderPdf(template: {
  content: string;
  data?: Record<string, unknown>;
}): Promise<Buffer> {
  await initialized;

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
