import { Component, OnInit } from '@angular/core';
import { Login } from '../DTO/Login';
import { LoginService } from './login.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  public model: Login = new Login()
  public title: String = "2Brains"

  constructor(private loginService: LoginService, private router: Router) { }

  public validate(): void{
    this.loginService.login(this.model).subscribe(
      token => {
        localStorage.setItem('token', token['token'])
        this.router.navigate(['/users']);
      }
    );
  }
}
