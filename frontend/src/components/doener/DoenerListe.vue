<!-- src/components/doener/DoenerListe.vue -->
<template>
  <table>
    <thead>
      <tr>
        <th>ID</th>
        <th>Bezeichnung</th>
        <th>Preis</th>
        <th>Vegetarisch</th>
        <th>Verfügbar</th>
        <th>Zutaten</th>
        <th>?</th>
      </tr>
    </thead>
    <tbody>
      <DoenerListeZeile v-for="eintrag in doener" :key="eintrag.id" :eintrag="eintrag" />
    </tbody>
  </table>
</template>

<script setup lang="ts">
import DoenerListeZeile from './DoenerListeZeile.vue'
import type { IDoenerDTD } from '@/stores/IDoenerDTD'
defineProps<{
  doener: IDoenerDTD[]
}>()
</script>
