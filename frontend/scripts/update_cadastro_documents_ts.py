from pathlib import Path

path = Path('src/app/components/beneficiario-cadastro/beneficiario-cadastro.component.ts')
text = path.read_text(encoding='utf-8')
old_name_block = """  private documentNameKey(name?: string): string {
    return (name ?? '').trim().toLowerCase();
      }
      """
      new_name_block = """  private documentNameKey(name?: string): string {
          return (name ?? '').trim().toLowerCase();
            }
            
  getDocumentAttachment(tipo: string): { control: FormGroup; index: number } | null {
      const chave = this.documentNameKey(tipo);
          for (let index = 0; index < this.anexos.length; index += 1) {
                const control = this.anexos.at(index) as FormGroup;
                      if (this.documentNameKey(control.get('nome')?.value) === chave) {
                              return { control, index };
                                    }
                                        }
                                            return null;
                                              }
                                              """
                                              if old_name_block not in text:
                                                      raise SystemExit('documentNameKey block not found')
                                                      text = text.replace(old_name_block, new_name_block, 1)
                                                      old_upload_block = """  onDocumentTypeFileSelected(event: Event): void {
                                                          const input = event.target as HTMLInputElement;
                                                              const file = input.files?.[0];
                                                                  if (!file) return;
                                                                      if (!this.documentoTipoSelecionado) {
                                                                            this.feedback = 'Selecione o tipo de documento antes de anexar.';
                                                                                  input.value = '';
                                                                                        return;
                                                                                            }
                                                                                                const existingIndex = this.anexos.controls.findIndex(
                                                                                                      (control) =>
                                                                                                              this.documentNameKey(control.get('nome')?.value) ===
                                                                                                                      this.documentNameKey(this.documentoTipoSelecionado),
                                                                                                                          );
                                                                                                                              let indexToUse = existingIndex;
                                                                                                                                  if (indexToUse < 0) {
                                                                                                                                        this.anexos.push(
                                                                                                                                                this.buildDocumentControl({ nome: this.documentoTipoSelecionado, obrigatorio: false }),
                                                                                                                                                      );
                                                                                                                                                            indexToUse = this.anexos.length - 1;
                                                                                                                                                                }
                                                                                                                                                                    const control = this.anexos.at(indexToUse) as FormGroup;
                                                                                                                                                                        this.applyFileToDocument(control, file, indexToUse);
                                                                                                                                                                            input.value = '';
                                                                                                                                                                              }
                                                                                                                                                                              """
                                                                                                                                                                              new_upload_block = """  onDocumentTypeFileSelected(event: Event, tipo?: string): void {
                                                                                                                                                                                  const input = event.target as HTMLInputElement;
                                                                                                                                                                                      const file = input.files?.[0];
                                                                                                                                                                                          if (!file) return;
                                                                                                                                                                                              const previousTipo = this.documentoTipoSelecionado;
                                                                                                                                                                                                  if (tipo) {
                                                                                                                                                                                                        this.documentoTipoSelecionado = tipo;
                                                                                                                                                                                                            }
                                                                                                                                                                                                                if (!this.documentoTipoSelecionado) {
                                                                                                                                                                                                                      this.feedback = 'Selecione o tipo de documento antes de anexar.';
                                                                                                                                                                                                                            input.value = '';
                                                                                                                                                                                                                                  if (tipo) {
                                                                                                                                                                                                                                          this.documentoTipoSelecionado = previousTipo;
                                                                                                                                                                                                                                                }
                                                                                                                                                                                                                                                      return;
                                                                                                                                                                                                                                                          }
                                                                                                                                                                                                                                                              const existingIndex = this.anexos.controls.findIndex(
                                                                                                                                                                                                                                                                    (control) =>
                                                                                                                                                                                                                                                                            this.documentNameKey(control.get('nome')?.value) ===
                                                                                                                                                                                                                                                                                    this.documentNameKey(this.documentoTipoSelecionado),
                                                                                                                                                                                                                                                                                        );
                                                                                                                                                                                                                                                                                            let indexToUse = existingIndex;
                                                                                                                                                                                                                                                                                                if (indexToUse < 0) {
                                                                                                                                                                                                                                                                                                      this.anexos.push(
                                                                                                                                                                                                                                                                                                              this.buildDocumentControl({ nome: this.documentoTipoSelecionado, obrigatorio: false }),
                                                                                                                                                                                                                                                                                                                    );
                                                                                                                                                                                                                                                                                                                          indexToUse = this.anexos.length - 1;
                                                                                                                                                                                                                                                                                                                              }
                                                                                                                                                                                                                                                                                                                                  const control = this.anexos.at(indexToUse) as FormGroup;
                                                                                                                                                                                                                                                                                                                                      this.applyFileToDocument(control, file, indexToUse);
                                                                                                                                                                                                                                                                                                                                          input.value = '';
                                                                                                                                                                                                                                                                                                                                              if (tipo) {
                                                                                                                                                                                                                                                                                                                                                    this.documentoTipoSelecionado = previousTipo;
                                                                                                                                                                                                                                                                                                                                                        }
                                                                                                                                                                                                                                                                                                                                                          }
                                                                                                                                                                                                                                                                                                                                                          """
                                                                                                                                                                                                                                                                                                                                                          if old_upload_block not in text:
                                                                                                                                                                                                                                                                                                                                                                  raise SystemExit('onDocumentTypeFileSelected block not found')
                                                                                                                                                                                                                                                                                                                                                                  text = text.replace(old_upload_block, new_upload_block, 1)
                                                                                                                                                                                                                                                                                                                                                                  path.write_text(text, encoding='utf-8')
                                                                                                                                                                                                                                                                                                                                                                  print('typescript updated')
                                                                                                                                                                                                                                                                                                                                                                  