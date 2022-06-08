export class UpdateBibelleseRequest {
  id: string | undefined
  bibelabschnitt: string | undefined
  lieblingsverse: string[] = []
  versText: string[] = []
  labels: string[] = []
  kommentar: string | undefined
  leser: string | undefined
}
