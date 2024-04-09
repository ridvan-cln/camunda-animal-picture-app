import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  title = 'Camunda Animal Picture App';
  processInstanceKey: string = '';
  imageUrl: string = '';
  notFound: boolean = false;

  constructor(private http: HttpClient) {}

  startProcess() {
    this.http.post("/api/animal-picture-process", {}, {responseType: 'text'}).subscribe(
      data => {
        this.processInstanceKey = data;
        this.imageUrl = '';
        this.notFound = false;
      },
      error => {
        console.error('Error:', error);
      }
    );
  }

  getPicture() {
    this.http.get("/api/animal-picture-process/" + this.processInstanceKey, {responseType: 'blob'}).subscribe(
      data => {
        let reader = new FileReader();
        reader.addEventListener("load", () => {
          this.imageUrl = reader.result as string;
        }, false);
  
        if (data) {
          reader.readAsDataURL(data);
        }
        this.notFound = false;
      },
      error => {
        console.error('Error:', error);
        if (error.status === 404) {
          this.notFound = true;
          this.imageUrl = '';
        }
      }
    );
  }
}
