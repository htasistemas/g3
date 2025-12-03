import packageJson from '../../package.json';

const browserLocation = typeof window !== 'undefined' ? window.location : null;
const isLocalhost = browserLocation
  ? ['localhost', '127.0.0.1'].includes(browserLocation.hostname)
  : true;

export const environment = {
  production: true,
  apiUrl: isLocalhost ? 'http://localhost:3000' : browserLocation?.origin ?? 'http://localhost:3000',
  version: packageJson.version
};
