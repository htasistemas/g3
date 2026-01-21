import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { OperadorChamadasComponent } from './pages/operador-chamadas/operador-chamadas.component';
import { PainelSenhaComponent } from './pages/painel-senha/painel-senha.component';

@NgModule({
  declarations: [AppComponent, OperadorChamadasComponent, PainelSenhaComponent],
  imports: [BrowserModule, HttpClientModule, FormsModule, AppRoutingModule],
  bootstrap: [AppComponent]
})
export class AppModule {}
