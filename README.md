# Bill Utility Manager (BUM)

Utility Manager è un'applicazione Java basata su Spring Boot progettata per automatizzare, calcolare e ripartire con precisione matematica le spese delle utenze (in particolare l'energia elettrica) tra due utenti, basandosi su letture di sotto-contatori.

## Motivazione
La gestione delle bollette tra più utenti presenta spesso un problema fondamentale: il **Disallineamento Temporale**.

1.  **Le Bollette** coprono periodi fissi (es. 01 Ottobre - 30 Novembre).
2.  **Le Letture** degli utenti avvengono in date arbitrarie (es. 15 Ottobre, 11 Gennaio).

I metodi tradizionali (calcoli manuali su Excel o stime forfettarie) spesso portano a errori di attribuzione.

**Utility Manager** risolve questo problema disaccoppiando le date di lettura dalle date di fatturazione attraverso algoritmi di interpolazione lineare temporale.

## Funzionamento e Metodologia

Il sistema opera in due fasi distinte: **Interpolazione Temporale** e **Ripartizione Finanziaria**.

### 1. Interpolazione Lineare (Consumption Engine)
Il sistema calcola il consumo esatto per il periodo di fatturazione, indipendentemente da quando sono state prese le letture.
Assume un consumo lineare tra due letture consecutive.

Date due letture:
- $R_1$ alla data $d_1$
- $R_2$ alla data $d_2$

Il valore del contatore $V$ per una data target $d_t$ (es. fine bolletta) è calcolato come:

$$
V(d_t) = R_1 + \left( \frac{R_2 - R_1}{d_2 - d_1} \right) \times (d_t - d_1)
$$

Dove:
- $\frac{R_2 - R_1}{d_2 - d_1}$ rappresenta lo **Slope** (consumo medio giornaliero nel periodo).

> **Nota:** Il sistema gestisce automaticamente la logica "Zero-Sum". Eventuali discrepanze dovute a picchi di consumo non lineari vengono automaticamente compensate nel calcolo della bolletta successiva, garantendo che nessun kWh vada perso o pagato due volte nel lungo periodo.

### 2. Ripartizione Costi (Bill Splitter)
Una volta determinato il consumo dell'inquilino ($C_{tenant}$) rispetto al consumo totale della bolletta ($C_{total}$), i costi vengono ripartiti secondo il principio di proporzionalità e competenza:

1.  **Costi Variabili (Materia Energia, Accise, IVA):**
    Ripartiti in base alla percentuale di consumo effettivo.
    $$
    Ratio = \frac{C_{tenant}}{C_{total}}
    $$
    $$
    Costo_{tenant} = (CostiVariabili_{totali}) \times Ratio
    $$

2.  **Costi Fissi (Trasporto, Quota Potenza):**
    Ripartiti equamente (Split 50/50), poiché il servizio è garantito indipendentemente dal consumo.

3.  **Costi Personali:**
    - **Canone RAI:** Attribuito interamente al proprietario (residente).
    - **Bonus Sociale:** Attribuito interamente al proprietario (intestatario ISEE).

## Stack

* **Java 21** (LTS)
* **Spring Boot 3.4+**
* **Maven**
* **JUnit 5**