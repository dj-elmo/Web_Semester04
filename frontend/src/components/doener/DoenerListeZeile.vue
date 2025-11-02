<!-- src/components/doener/DoenerListeZeile.vue -->
<template>
    <!-- Hauptzeile -->
    <tr>
      <td>{{ eintrag.id }}</td>
      <td>{{ eintrag.bezeichnung }}</td>
      <td>{{ eintrag.preis }}</td>
      <td>{{ vegetarizitaetLabel(eintrag.vegetarizitaet) }}</td>
      <td>{{ eintrag.verfuegbar }}</td>
      <td>
        <button @click="zeigeZutaten = !zeigeZutaten" title="Zutaten anzeigen/verstecken">?</button>
      </td>
    </tr>

    <!-- Zutatenzeile (aufklappbar) -->
    <tr v-if="zeigeZutaten">
      <td colspan="4">
        <ul>
          <li v-for="z in eintrag.zutaten" :key="z.ean">
            <img
              :src="`/images/zutaten/${z.ean}.png`"
              :alt="z.name"
              width="32"
              height="32"
              style="vertical-align: middle; margin-right: 8px"
            />
            <a :href="`https://de.wikipedia.org/wiki/Special:Search/${encodeURIComponent(z.name)}`" target="_blank">
              {{ z.name }}
            </a>
            ({{ vegetarizitaetLabel(z.vegetarizitaet) }})
          </li>
        </ul>
      </td>
    </tr>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import type { IDoenerDTD } from '@/stores/IDoenerDTD'
defineProps<{
  eintrag: IDoenerDTD
}>()

const zeigeZutaten = ref(false)

function vegetarizitaetLabel(code: number): string {
  switch (code) {
    case 0:
      return 'unvegetarisch'
    case 1:
      return 'vegetarisch'
    case 2:
      return 'vegan'
    default:
      return '-'
  }
}
</script>

