import { Injectable } from '@angular/core';
import { environment } from "../../../environments/environment";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { GetJockeysResponse } from "../../models/jockey/GetJockeysResponse";

@Injectable({
  providedIn: 'root'
})
export class JockeyService {
  private API_URL = environment.DATASTORE_API;

  constructor(private httpClient: HttpClient) { }

  fetchJockeys(offset: number, max: number): Observable<GetJockeysResponse> {
    return this.httpClient
      .get<any>(
        `${this.API_URL}/jockeys?offset=${offset}&max=${max}`
      )
  }
}