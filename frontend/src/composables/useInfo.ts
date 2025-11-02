import { ref, readonly} from 'vue'
import { MagicString } from 'vue/compiler-sfc'

const info = ref('Wilkommen beim Döner-Verleih!')

export function useInfo(){

    function loescheInfo(){
        info.value = ''
    }

    function setzeInfo(msg: string){
        info.value = msg
    }
    
    return{
        info: readonly(info),
        loescheInfo,
        setzeInfo
    }
}