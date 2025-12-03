import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { SystemParameterService } from '../services/system-parameter.service';

export const libraryEnabledGuard: CanActivateFn = () => {
  const params = inject(SystemParameterService);
  const router = inject(Router);

  if (params.isLibraryEnabled()) {
    return true;
  }

  return router.createUrlTree(['/dashboard/visao-geral']);
};
