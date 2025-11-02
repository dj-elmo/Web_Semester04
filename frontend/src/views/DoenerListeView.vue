<template>
    <div>
      <h1 class="title">Unser aktuelles Dönerangebot</h1>
      <p class="heading">Nur wenige Klicks trennen Sie von Ihrem Traumdöner</p>
    </div>
    
    <div>
      <img 
        src="@/assets/doener-ritter-tiere.png" 
        alt="Illustration von Döner, Rittern und Tieren" 
      />
    </div>

    <div>
      <input
        v-model="suchbegriff"
        type="text"
        placeholder="Döner oder Zutat suchen..."
        class="border rounded px-2 py-1 w-full"
      />
      <button @click="resetSuche" class="bg-gray-200 px-3 py-1 rounded">
        Reset
      </button>
    </div>
    
    <div v-if="doenerliste">
      <DoenerListe :doener="gefilterteDoener" />
    </div>

</template>
  
<script setup lang="ts">
  import { onMounted, computed, ref } from 'vue'
  import DoenerListe from '@/components/doener/DoenerListe.vue'
  import { useDoenerStore } from '@/stores/doenerstore'
  import type { IDoenerDTD } from '@/stores/IDoenerDTD'

  const suchbegriff = ref('')


  const doenerStore = useDoenerStore()

  onMounted(() => {
    doenerStore.updateDoenerListe()
  })

  const doenerliste = computed(() => doenerStore.doenerdata.doenerliste)

  // Computed Property für die Filterung
  const gefilterteDoener = computed(() => {
  const begriff = suchbegriff.value.trim().toLowerCase()

  if (!begriff) {
    return doenerStore.doenerdata.doenerliste
  }

  return doenerStore.doenerdata.doenerliste.filter((doener: IDoenerDTD) => {
    const nameMatch = doener.bezeichnung.toLowerCase().includes(begriff)
    const zutatenMatch = doener.zutaten.some(zutat =>
      zutat.name.toLowerCase().includes(begriff)
    )
    return nameMatch || zutatenMatch
  })
})

// Reset-Funktion
const resetSuche = () => {
  suchbegriff.value = ''
}
</script>

<style scoped>
.title {
  font-size: 2rem;
  font-weight: bold;
  margin-bottom: 0.5rem;
}

.heading {
  font-size: 1.2rem;
  margin-bottom: 1rem;
}
</style>