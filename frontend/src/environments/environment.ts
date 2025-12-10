import packageJson from '../../package.json';

const browserLocation = typeof window !== 'undefined' ? window.location : null;
const runtimeApiUrl =
  typeof window !== 'undefined'
    ? // Permite configurar o endpoint em tempo de execução via window.__env.apiUrl
      ((window as any).__env?.apiUrl as string | undefined)
    : undefined;

const apiPort = browserLocation?.port === '4200' ? '3000' : browserLocation?.port;
const apiUrlFromLocation = browserLocation
  ? `${browserLocation.protocol}//${browserLocation.hostname}${apiPort ? `:${apiPort}` : ''}`
  : null;

export const environment = {
  production: true,
  apiUrl: runtimeApiUrl ?? apiUrlFromLocation ?? 'http://localhost:3000',
  version: packageJson.version
};
