import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [ FormsModule ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'Camunda Animal Picture App';

  selectedAnimal!: string;

  sendAnimalRequest() {
    console.log('sendAnimalRequest');
    console.log('selectedAnimal:'+ this.selectedAnimal);
  }
}
