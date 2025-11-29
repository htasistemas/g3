import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ThemeService } from '../../services/theme.service';

@Component({
  selector: 'app-system-parameters',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './system-parameters.component.html',
  styleUrl: './system-parameters.component.scss'
})
export class SystemParametersComponent {
  constructor(public readonly themeService: ThemeService) {}

  toggleTheme(): void {
    this.themeService.toggleTheme();
  }
}
