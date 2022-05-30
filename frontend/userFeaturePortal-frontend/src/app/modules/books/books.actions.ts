import {createAction, props} from "@ngrx/store";
import {GetBooksErrorResponse, GetBooksResponse} from "./getBooks/get-books-response";
import {AddBookRequest} from "./addBooks/add-book-request";
import {AddBookErrorResponse, AddBookResponse} from "./addBooks/add-book-response";
import {DeleteBookErrorResponse, DeleteBookResponse} from "./deleteBook/delete-book-response";
import {DeleteBookRequest} from "./deleteBook/delete-book-request";
import {DeleteBooksErrorResponse, DeleteBooksResponse} from "./deleteBooks/delete-books-response";

export const getBooksAction = createAction('Get books');
export const getBooksResponseAction = createAction('Get list of books', props<GetBooksResponse>())
export const loadGetBooksErrorAction = createAction('Load Get books failure', props<GetBooksErrorResponse>())

export const addBookAction = createAction('Add book', props<AddBookRequest>());
export const addBookResponseAction = createAction('Get response if book addition succeed', props<AddBookResponse>());
export const loadAddBookErrorAction = createAction('Load Add book failure', props<AddBookErrorResponse>());

export const deleteBookAction = createAction('Delete one book', props<DeleteBookRequest>());
export const deleteBookResponseAction = createAction('Get response if deleting of one book succeed', props<DeleteBookResponse>());
export const loadDeleteBookErrorAction = createAction('Load Delete book failure', props<DeleteBookErrorResponse>());

export const deleteBooksAction = createAction('Delete all books');
export const deleteBooksResponseAction = createAction('Get response if deleting of all books succeed', props<DeleteBooksResponse>());
export const loadDeleteBooksErrorAction = createAction('Load Delete books failure', props<DeleteBooksErrorResponse>());

export const updateBookAction = createAction('Update book', props<AddBookRequest>());
export const updateBookResponseAction = createAction('Get response if book updating succeed', props<AddBookResponse>());
export const loadUpdateBookErrorAction = createAction('Load Update book failure', props<AddBookErrorResponse>());
