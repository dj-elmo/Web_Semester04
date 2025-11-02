import { defineStore } from 'pinia'
import { useInfo } from '@/composables/useInfo'
import { Client } from '@stomp/stompjs'
import type { IMessage } from '@stomp/stompjs'
import type { IFrontendNachrichtEvent } from '@/services/IFrontendNachrichtEvent'



export interface IZutatDTD {
  ean: string
  name: string
  vegetarizitaet: number
}

export interface IDoenerDTD {
  id: number
  bezeichnung: string
  preis: number
  vegetarizitaet: number
  zutaten: IZutatDTD[]
  verfuegbar: number
}

let stompClient: Client | null = null

export const useDoenerStore = defineStore('doenerstore', {
  state: () => ({
    doenerdata: {
      ok: false,
      doenerliste: [] as IDoenerDTD[],
    },
  }),
  actions: {
    async updateDoenerListe() {
      const { setzeInfo } = useInfo()
      console.log('Starte Abruf der Dönerliste...')
      try {
        const resp = await fetch(`/api/doener`)
        if (!resp.ok) {
          setzeInfo(resp.statusText)
          console.error('Fehler beim Abruf der Dönerliste:', resp.statusText)
          throw new Error(resp.statusText)
        }
        const doenerListe = await resp.json() as IDoenerDTD[]// Array aus Doener-Objekten

        this.doenerdata.ok = true
        this.doenerdata.doenerliste = doenerListe
        console.log('Dönerliste erfolgreich geladen:', doenerListe)

        this.startDoenerLiveUpdate()
      } catch (reason) {
          console.log(reason)
        this.doenerdata.ok = false
        this.doenerdata.doenerliste = []
      }
    },
    async startDoenerLiveUpdate() {
      const wsurl = `ws://${window.location.host}/stompbroker`;
      if (stompClient && stompClient.active) {
        // Bereits verbunden
        return
      }
      console.log('Starte STOMP-Client mit URL:', wsurl)
      stompClient = new Client({
        brokerURL: wsurl,
      })

      const doenerStore = useDoenerStore()
      stompClient.onConnect = () => {
        stompClient?.subscribe('/topic/doener', (message: IMessage) => { //? sorgt dafür, dass subscribe nur aufgerufen wird, wenn stompClient nicht null ist
          console.log('STOMP-Event empfangen:', message.body)

          try {
            const event: IFrontendNachrichtEvent = JSON.parse(message.body)
            console.log(JSON.stringify(event))

            //Nur bei Nachrichten mit typ === 'DOENER' die Liste aktualisieren
            if (event.typ === 'DOENER') {
              console.log('DOENER-Event erkannt, aktualisiere Dönerliste...')
              doenerStore.updateDoenerListe()
            }
          } catch (error) {
            const { setzeInfo } = useInfo()
            console.error('Fehler beim Verarbeiten der Live-Update-Nachricht:', error)
            setzeInfo('Fehler beim Verarbeiten der Live-Update-Nachricht: ' + (error instanceof Error ? error.message : error)) //Wenn error ein Error Objekt ist, dann dessen message ausgeben
          }
        })
      }
      stompClient.activate()
      console.log('STOMP-Client aktiviert')

    },
  },
})
