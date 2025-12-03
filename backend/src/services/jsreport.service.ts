import jsreport from 'jsreport-core';
import jsreportChrome from 'jsreport-chrome-pdf';

const instance = jsreport();
instance.use(jsreportChrome());

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
