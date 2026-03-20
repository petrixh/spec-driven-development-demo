import { EndpointRequestInit as EndpointRequestInit_1 } from "@vaadin/hilla-frontend";
import type MovieCardDto_1 from "./com/example/specdriven/catalog/application/MovieCardDto.js";
import type MovieDetailsDto_1 from "./com/example/specdriven/catalog/application/MovieDetailsDto.js";
import client_1 from "./connect-client.default.js";
async function getMovieDetails_1(movieId: number, init?: EndpointRequestInit_1): Promise<MovieDetailsDto_1> { return client_1.call("MovieBrowserEndpoint", "getMovieDetails", { movieId }, init); }
async function listBrowseableMovies_1(init?: EndpointRequestInit_1): Promise<Array<MovieCardDto_1>> { return client_1.call("MovieBrowserEndpoint", "listBrowseableMovies", {}, init); }
export { getMovieDetails_1 as getMovieDetails, listBrowseableMovies_1 as listBrowseableMovies };
