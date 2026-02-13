import packageJson from '../../package.json';

const obterApiUrlRuntime = (): string | undefined => {
  if (typeof window === 'undefined') return undefined;
  // Permite configurar o endpoint em tempo de execucao via window.__env.apiUrl
  const apiUrl = (window as any).__env?.apiUrl as string | undefined;
  const normalizado = (apiUrl ?? '').trim();
  if (!normalizado || normalizado === '__ENV_API_URL__') return undefined;
  return normalizado;
};

export const environment = {
  production: true,
  apiUrl: obterApiUrlRuntime() ?? '/api',
  version: packageJson.version
};
