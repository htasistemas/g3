import { inject } from '@angular/core';
import { CanActivateFn, CanMatchFn, Router, UrlTree } from '@angular/router';
import { SystemParameterService } from '../services/system-parameter.service';

export const libraryEnabledGuard: CanActivateFn & CanMatchFn = () => {
  const params = inject(SystemParameterService);
  const router = inject(Router);

  return libraryAccessResult(params.isLibraryEnabled(), router);
};

function libraryAccessResult(isEnabled: boolean, router: Router): true | UrlTree {
  if (isEnabled) {
    return true;
  }

  return router.createUrlTree(['/dashboard/visao-geral']);
}
