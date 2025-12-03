import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateFn, CanMatchFn, Route, Router, UrlSegment, UrlTree } from '@angular/router';
import { SystemParameterService } from '../services/system-parameter.service';

const LIBRARY_PATH = 'administrativo/biblioteca';

export const libraryEnabledCanMatch: CanMatchFn = (route: Route, segments: UrlSegment[]) => {
  if (!isLibraryRoute(route, segments)) {
    return true;
  }

  const params = inject(SystemParameterService);
  const router = inject(Router);

  return libraryAccessResult(params.isLibraryEnabled(), router);
};

export const libraryEnabledCanActivate: CanActivateFn = (route: ActivatedRouteSnapshot) => {
  const params = inject(SystemParameterService);
  const router = inject(Router);

  if (!isLibraryRoute(route.routeConfig ?? {}, route.url)) {
    return true;
  }

  return libraryAccessResult(params.isLibraryEnabled(), router);
};

function isLibraryRoute(route: Route, segments: UrlSegment[]): boolean {
  const configuredPath = route.path ?? '';
  const attemptedPath = segments.map((segment) => segment.path).join('/');

  return configuredPath.startsWith(LIBRARY_PATH) || attemptedPath.startsWith(LIBRARY_PATH);
}

function libraryAccessResult(isEnabled: boolean, router: Router): true | UrlTree {
  if (isEnabled) {
    return true;
  }

  return router.createUrlTree(['/dashboard/visao-geral']);
}
