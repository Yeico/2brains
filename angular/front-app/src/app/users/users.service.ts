import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from "../DTO/User";

@Injectable()
export class UserService{
    private endPoint: string = 'https://twobrains-spring-boot-heroku.herokuapp.com/api/users'
    constructor(private http: HttpClient) {}

    getUsers(token: string): Observable<User[]> {
        let httpHeader = new HttpHeaders({
            'Authorization': 'Bearer ' + token
        })
        return this.http.get<User[]>(this.endPoint, {headers: httpHeader});
    }
}