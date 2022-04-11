import {createAction, props} from "@ngrx/store";
import {GetBooksErrorResponse, GetBooksResponse} from "./getBooks/get-books-response";
import {DeleteLogRequest} from "../logging/deleteLog/dto/delete-log-request";
import {AddBookRequest} from "./addBooks/add-book-request";
import {AddBookErrorResponse, AddBookResponse} from "./addBooks/add-book-response";
import {DeleteBookErrorResponse, DeleteBookResponse} from "./deleteBook/delete-book-response";

export const getBooksAction = createAction('Get books');
export const getBooksResponseAction = createAction('Get list of books', props<GetBooksResponse>())
export const loadGetBooksErrorAction = createAction('Load Get books failure', props<GetBooksErrorResponse>())

export const addBookAction = createAction('Add book', props<AddBookRequest>());
export const addBookResponseAction = createAction('Get response if book addition succeed', props<AddBookResponse>());
export const loadAddBookErrorAction = createAction('Load Add book failure', props<AddBookErrorResponse>());

export const deleteBookAction = createAction('Delete one book', props<DeleteLogRequest>());
export const deleteBookResponseAction = createAction('Get response if deleting of one book succeed', props<DeleteBookResponse>());
export const loadDeleteBookErrorAction = createAction('Load Delete book failure', props<DeleteBookErrorResponse>());
