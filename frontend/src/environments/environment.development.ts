import packageJson from '../../package.json';

const browserLocation = typeof window !== 'undefined' ? window.location : null;
const apiUrlFromLocation = browserLocation
  ? `${browserLocation.protocol}//${browserLocation.hostname}:3000`
  : null;

export const environment = {
  production: false,
  apiUrl: apiUrlFromLocation ?? 'http://localhost:3000',
  version: packageJson.version
};
