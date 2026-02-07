import packageJson from '../../package.json';

const browserLocation = typeof window !== 'undefined' ? window.location : null;
const obterApiUrlRuntime = (): string | undefined => {
  if (typeof window === 'undefined') return undefined;
  // Permite configurar o endpoint em tempo de execucao via window.__env.apiUrl
  const apiUrl = (window as any).__env?.apiUrl as string | undefined;
  const normalizado = (apiUrl ?? '').trim();
  if (!normalizado || normalizado === '__ENV_API_URL__') return undefined;
  return normalizado;
};

const apiPort = browserLocation?.port === '4200' ? '8080' : browserLocation?.port;
const apiUrlFromLocation = browserLocation
  ? `${browserLocation.protocol}//${browserLocation.hostname}${apiPort ? `:${apiPort}` : ''}`
  : null;

export const environment = {
  production: true,
  apiUrl: obterApiUrlRuntime() ?? apiUrlFromLocation ?? 'http://localhost:3000',
  version: packageJson.version,
  googleClientId: '1026369251340-2eskbj74ierlra1i9fm0aas29ucvnudf.apps.googleusercontent.com'
};
