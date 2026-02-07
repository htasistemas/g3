import packageJson from '../../package.json';

const obterApiUrlRuntime = (): string | undefined => {
  if (typeof window === 'undefined') return undefined;
  const apiUrl = (window as any).__env?.apiUrl as string | undefined;
  const normalizado = (apiUrl ?? '').trim();
  if (!normalizado || normalizado === '__ENV_API_URL__') return undefined;
  return normalizado;
};

export const environment = {
  production: false,
  apiUrl: obterApiUrlRuntime() ?? 'http://localhost:8080',
  version: packageJson.version,
  googleClientId: '1026369251340-2eskbj74ierlra1i9fm0aas29ucvnudf.apps.googleusercontent.com'
};
