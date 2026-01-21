import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { OperadorChamadasComponent } from './pages/operador-chamadas/operador-chamadas.component';
import { PainelSenhaComponent } from './pages/painel-senha/painel-senha.component';

const routes: Routes = [
  { path: '', redirectTo: 'operador/chamadas', pathMatch: 'full' },
  { path: 'operador/chamadas', component: OperadorChamadasComponent },
  { path: 'painel/senha', component: PainelSenhaComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule {}
