import packageJson from '../../package.json';

const browserLocation = typeof window !== 'undefined' ? window.location : null;
const apiPort = browserLocation?.port === '4200' ? '3000' : browserLocation?.port;
const apiUrlFromLocation = browserLocation
  ? `${browserLocation.protocol}//${browserLocation.hostname}${apiPort ? `:${apiPort}` : ''}`
  : null;

export const environment = {
  production: true,
  apiUrl: apiUrlFromLocation ?? 'http://localhost:3000',
  version: packageJson.version
};
