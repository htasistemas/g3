const fs = require('fs');
const path = require('path');
const { spawnSync } = require('child_process');

const projectRoot = path.join(__dirname, '..');
const nodeModulesRoot = path.join(projectRoot, 'node_modules');
const requiredPackages = ['zone.js'];

const missingPackages = requiredPackages.filter((pkg) => {
  const pkgJson = path.join(nodeModulesRoot, pkg, 'package.json');
  return !fs.existsSync(pkgJson);
});

if (missingPackages.length === 0) {
  process.exit(0);
}

console.log(
  `Missing dependencies detected (${missingPackages.join(', ')}). ` +
    'Running npm install to restore them...'
);

const npmCommand = process.platform === 'win32' ? 'npm.cmd' : 'npm';
const result = spawnSync(npmCommand, ['install'], {
  cwd: projectRoot,
  stdio: 'inherit',
});

process.exit(result.status ?? 1);
