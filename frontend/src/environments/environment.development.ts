import packageJson from '../../package.json';

const runtimeApiUrl =
  typeof window !== 'undefined'
    ? ((window as any).__env?.apiUrl as string | undefined)
    : undefined;

export const environment = {
  production: false,
  apiUrl: runtimeApiUrl ?? 'http://localhost:8080',
  version: packageJson.version
};
