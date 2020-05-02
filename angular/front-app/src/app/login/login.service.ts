import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Login } from "../DTO/Login";
import { Observable } from 'rxjs';

@Injectable()
export class LoginService {
    private httpHeader = new HttpHeaders({
        'Content-Type': 'application/json'
    })
    
    constructor(private http: HttpClient) {}

    login(login: Login): Observable<string> {
        let endPoint: string = "https://twobrains-spring-boot-heroku.herokuapp.com/api/login?email=" + login.userName + "&password=" + login.password
        let params = new HttpParams().set("email", login.userName).set("password", login.password) 
        
        return this.http.post<string>(endPoint, 
            params, 
            {headers: this.httpHeader}
        );
    }
}