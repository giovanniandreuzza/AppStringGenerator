# Data Mining - R

## Pulizia environment
```R
rm(list=ls()) # rimuovo tutto
```

## Librerie da importare

```R
library(dplyr)
library(tidyverse)
library(data.table)
library(DataExplorer)
library(lubridate)
library(janitor)
```

## Importare i dati
```R
# Importare i dati da un file Excel
dati <- read_excel("/Users/beatrice/Desktop/cucina.xlsx")

# Importare i dati da un file CSV
dati=read.csv("/Users/beatrice/Desktop/Girls/crm_classificazione/crm.csv", header=T, sep=";", dec=".",  stringsAsFactors=TRUE)

str(dati)
```

## Sistemazione Dataset

#### Estrarre i nomi delle colonne dal data frame
```R
colnames(data)
```
#### Estrarre i nomi delle colonne dal data frame dopo essere stati puliti e standardizzati
```R
colnames(data) <- make.names(colnames(data), unique = TRUE)
```

#### Imposto a NULL una colonna del data frame passando il suo nome, ad esempio X
```R
data$X = NULL
```

#### Tabella di frequenza

Una colonna
```R
table(data$X)

# Esempio
# ["home", "work", "home", "school", "home", "work"]

# home  work school
#    3     2      1
```

Due colonne
```R
table(data$X, data$Y)

# Esempio
# |---|---|
# | X | Y |
# |---|---|
# | A | M |
# | A | M |
# | B | L |
# | B | M |
# | A | L |
# |---|---|

#   Y
# X	 | M | L
# ---|---|---|
# A  | 2 | 1 |
# ---|---|---|
# B  | 1 | 1 |
# ---|---|---|

```

#### One variable out
```R
dati <- dati %>% select(-xbimbumbamx,)
```

#### Some variables out
```R
dati = dati %>% select(-c(x1,x2,x3))

# Oppure
dati <- dati %>% select(-xbimbumbamx, -xbimbumbamx2)

# Oppure
dati <- subset(dati, select = -c(xbimbumbamx, xbimbumbamx2))    
```

#### One variable in 
```R
dati <- dati %>% select(xbimbumbamx)
```

#### Some variables in 
```R
dati <- dati %>% select(xbimbumbamx, xbimbumbamx2)
```


#### Rimuovo le colonne dalla X alla Y dal dataframe 'dati'
```R
dati = data[,-c(X:Y)]
# Oppure
dati <- dati %>% select(-(X:Y))

# Esempio
dati = data[,-c(42:81)]
# Oppure
dati <- dati %>% select(-(42:81))
```

#### Escludo una modalità di una variabile qualitativa
```R
table(dati$sesso, dati$morto) 
# terza modalità (non M e F)
dati=subset(dati, sesso!="No anotado")
dati$sesso=factor(dati$sesso)
```

#### Rinominare una colonna
```R
# Se si conosce il nome della colonna da cambiare
dati = dati %>% rename(y = da_cambiare) 

# Se si vuole rinominare la prima colonna
colnames(dati)[1] <- "y" 
```

#### Costruzione variabile risposta (tasso)
```R
dati$y.nuova = dati$y/danni$n.danni.0

dati$y.nuova[dati$n.danni.0 == 0] = 0

# elimino variabili associate alla risposta, utilizzate per costruirla o che permettono di prevederla
dati[,c("n.danni.0", "imp.sini.co", "n.dis.co")]=NULL
```

#### Assegnare macromodalità ad alcune modalità, per semplificare la risposta
```R
dati$y[dati$y=="Canceled"]="Suspended"
dati$y[dati$y=="Failed"]="Suspended"
dati$y[dati$y=="Live"]="Suspended"
```

#### Guardo se ci sono variabili ridondanti con troppe modalità, le elimino
```R
dim(table(dati$prof))
dati$prof=NULL
```

#### Trasformare in fattore
```R
dati$sesso=factor(dati$sesso)
```

#### Creo nuova variabile che invece di contare il num di prodotti, identifica la presenza di almeno una polizza
```R
dati$i.banca.1=(dati$n.banca.1>0)
table(dati$i.banca.1)
```

#### Se dataset troppo grande, prendo un sottocampione casuale
```R
righe=sample(1:nrow(dati),10000)  #scelgo 10000 righe
dati=dati[righe,]
```

#### Eliminare le variabili con una parte del nome in comune
```R
dati[,grep("aaa",names(dati))] = NULL
```

#### Rimuovo una modalità di una variabile (free)
```R
dati <- dati[dati$PRICE.DESCRIPTION != "Free",]
table(dati$PRICE.DESCRIPTION)

# Oppure
dati$PRICE.DESCRIPTION = droplevels(dati$PRICE.DESCRIPTION, exclude = "Free")
table(dati$PRICE.DESCRIPTION)
```

#### Selezione delle variabili da eliminare che contengono _500
```R
tutte = grep("_500", names(dati), value=T) # per avere i nomi delle posizioni mettere value T
dati = dati %>% select(-all_of(tutte))

# Oppure
dati[,grep("_500|_100",names(dati))]=NULL
```

#### Correggere un indicatore sbagliato nella variabile, correggo "." e metto ":"
```R
ora=gsub("\\.", ":", dati$ora)

# Seleziono solo l'ora e tralascio i minuti
ora=gsub(":.*","",ora)

# Se ora è character, la metto numerica
table(sprintf("%02s", ora))
dati$ora=as.numeric(ora)
```

#### Reimpostare livelli unici 
```R
livelli <- unique(dati$xbimbumbamx)
dati$xbimbumbamx <- factor(dati$xbimbumbamx, levels=livelli)
```

#### Cambiare la modalità di riferimento per poi applicare un modello lineare
```R
# Qui voglio la modalità riposo come quella di riferimento
dati$attività=relevel(dati$attività, ref="riposo")

# Cambiare il nome di una modalità di una variabile
levels(dati$POSITION)[levels(dati$POSITION)==""] <- "NS"
```

#### Esistono esplicative costanti?
```R
costant.search <- function(dataset){
  res <- c()
  for(i in 1:NCOL(dataset)){
    if( length(unique(dataset[,i])) == 1 ) { 
      res <- c(res, colnames(dataset[,i]))
   }
  }
  if(is.null(dim(res))) cat("Non sono state identificate variabili costanti")
  else res
}
costant.search(dati)
# se sì, le elimino
dati = dati%>%dplyr::select(-c(x1,x2,x3))
```

#### Filtrare nel dataset
```R
stima=stima %>% filter(stazione=="CB")
```

#### Aggiungere colonne
```R
dati <- bind_cols(y = dati$xbimbumbamx, dati)
```

#### Variabili fattoriali
```R
ind.fac <- sapply(dati, function(x) is.factor(x))
```

#### Variabili numeriche
```R
ind.num <- sapply(dati, function(x) is.numeric(x))
```

#### Quando voglio vedere la posizione ed eliminare da quella variabile in poi
```R
posizione_variabile=which(names(dati)== "nome_variabile") 
dati <- dati[, -seq(posizione_variabile, ncol(dati))]

# voglio vedere la posizione ed eliminare da quella variabile alla posizione di un'altra var
# Trova la posizione della colonna di partenza
posizione_partenza <- which(names(dati3) == "variabile_di_partenza")

# Trova la posizione della colonna di fine
posizione_fine <- which(names(dati3) == "variabile_di_fine")

# Elimina le colonne dall'intervallo trovato
dati <- dati[, -seq(posizione_partenza, posizione_fine)]
```

#### Variabili con nomi simili 
```R
# Ho 3 variabili con nomi simili, scelgo di tenere solo quella che inserisco in "nomisimili_importante", in "nomisimili" metto la parte comune 
indx.out <- !(names(dati[,grepl( "nomisimili" , names( dati ) )]) %in% "nomisimili_importante")
var.out <- names(dati[,grepl( "nomisimili" , names( dati ) )])[indx.out]
dati <- dati %>% select(-all_of(var.out))
```

#### Usare GREP per ricerche
```R
# indicatori di posizione
grep("[a-z]", letters)
grep("[1-5]", 1:50)

# indicatori logici 
grepl("[a-h]", letters)
grepl("[1-5]", 1:50)

# modificare vettori 
grepl("[a-h]", "ciccio",letters)
gsub("[1-5]", 100 ,1:50) ## trasforma un vettore numerico in caratteri

# ?
gsub(as.Date(dati$ora_GMT), "", dati$ora_GMT)

# ricerca di una determinata parola tra dentro a qualcosa
parole<-grep("parola",vettore_di_parole,val=T)

# togliere variabili con una stessa parola nel nome della variabile
dati[,grep("parola",names(dati))] = NULL
```

#### Quando voglio selezionare le righe per una determinata categoria della variabile
```R
dati = dati[dati$Stato=="Successful",]
#oppure
dati = dati %>% filter(Stato=="Successful") %>% select(-Stato)
```

#### Se voglio eliminare le righe per una determinata categoria della variabile
```R
#               0    1
#  Femenino   6475   52
#  Masculino  3343   59
#  No anotado   51    0

dati = subset(dati, sesso != "No anotado")
dati$sesso = factor(dati$sesso)
```

#### Campionare
```R
summary(data.frame(table(dati$comune)))

xbimbumbamx.piccoli <- names(which(table(dati$comune) <= 1000))
dim(dati[!(dati$comune %in% xbimbumbamx.piccoli), ])
dim(dati)

dati <- dati[!(dati$comune %in% xbimbumbamx.piccoli), ]
```

#### Categorizzare con quantili
```R
# # variabile da categorizzare
# cut(dati$Distance, 
#     breaks = quantile(dati$Distance, 0:4/4, na.rm = T),
#     include.lowest = T)

aa <- cut(dati$URINE.PH,
          breaks = quantile(dati$URINE.PH, 0:2/2, na.rm = T),
          include.lowest = T)

livelli <- c(levels(aa), "5")
aa <- factor(aa, levels = livelli)
aa[which(is.na(aa))] <- 5
aa
```


## Sistemazione NA

### Trova le variabili con NA, calcola la frequenza assoluta e relativa
```R
na_get = function(data){
  na_vars = sapply(data, function(col) sum(is.na(col)))
  na_vars = sort(na_vars[na_vars > 0])
  na_vars = data.frame(
    variabile = names(na_vars),
    freq_assoluta = as.numeric(na_vars),
    freq_relativa = round(as.numeric(na_vars)/nrow(data), 4)
  )
  na_vars
}
na_tab = na_get(dati)
na_tab
```

### Eliminare le righe con NA
```R
# Prima verifico QUANTE righe andrei ad eliminare, se un numero accettabile rispetto a n, elimino

q=nrow(dati)-nrow(na.omit(dati))
q # controllo q

# Elimina tutte le righe che contengono almeno un NA in qualsiasi colonna del data frame
dati = na.omit(dati)

# Oppure

# Elimina tutte le righe che contengono almeno un NA in una specifica colonna
dati = dati[!is.na(dati$variabile_con_NA),]
```

### Raggruppare valori mancanti (NA) in una variabile qualitativa, fattore o ordinale
```R
livelli <- c(levels(dati$SIN_INIEZIONE_DOLORE_INI), "Altro")

dati$SIN_INIEZIONE_DOLORE_INI <- factor(dati$SIN_INIEZIONE_DOLORE_INI, levels = livelli)

dati$SIN_INIEZIONE_DOLORE_INI[which(is.na(dati$SIN_INIEZIONE_DOLORE_INI))] <- "Altro"

dati$SIN_INIEZIONE_DOLORE_INI <- factor(dati$SIN_INIEZIONE_DOLORE_INI)

summary(dati$SIN_INIEZIONE_DOLORE_INI)
```

### Salvo nomi delle variabili che presentano NA e frequenze assolute
```R
var_NA <- na_tab[,1] # nomi delle variabili che presentano NA
freq_NA <- na_tab[,2]

#1) per le colonne con tanti NA elimino le variabili, invece che le osservazioni che presentano NA
var_NA <- na_tab[,1]
var_NA
dati <- dati %>% select(-all_of(var_NA))
dim(dati)
str(dati)

# Oppure

# Salvo nomi delle variabili che presentano NA e frequenze relative
var_NA <- na_dati[,1] # nomi delle variabili che presentano NA
freq_NA <- na_dati[,2]/dim(dati)[1]

var_NA_min_40 <- var_NA[freq_NA<0.40]
var_NA_magg_40 <- var_NA[freq_NA>=0.40]

# Elimino variabili con più del 40% di dati mancanti
dati = dati %>% select(-all_of(var_NA_magg_40))

# sotto il 40%, trattamento delle variabili qualitative
# Setto NA in una modalità "altro" per tutte le variabili qualitative
for (i in seq_along(dati)) {   
	if (!is.numeric(dati[,i])) {                 
		livelli <- c(levels(dati[,i]),"altro")
		dati[,i] <- factor(dati[,i], levels = livelli)
		dati[,i][which(is.na(dati[,i]))] <- "altro"
		dati[,i] <- factor(dati[,i])
	}
}
                                      
# Imputazione del valore agli NA
# Sostituisco NA con mediana per tutte le variabili numeriche, più robusta
for (i in seq_along(dati)) {   
	if (is.numeric(dati[,i])) {                 
    	mediana <- median(dati[,i], na.rm = TRUE)
		dati[,i] <- ifelse(is.na(dati[,i]), mediana, dati[,i])
	}
}

# Oppure 
for(i in 1:length(var_NA)){
  dati[is.na(dati[,var_NA[i]]),var_NA[i]] <- na.omit(dati[,var_NA[i]]) %>% median() #Calcola la mediana dei valori non mancanti della colonna corrente.
}

na_dati = trova_na(dati)
na_dati


# SOSTITUIRE LE OSSERVAZIONI ZERO INTESE COME DATI MANCANTI CON LA MEDIANA
table(dati$first_amount_spent[dati$first_amount_spent==0])
table(dati$first_amount_spent[dati$first_amount_spent==0])

fam = median(dati$first_amount_spent[dati$first_amount_spent!=0])
nop = median(dati$number_of_products[dati$number_of_products!=0])

dati$first_amount_spent[dati$first_amount_spent==0]=fam
dati$number_of_products[dati$number_of_products==0]=nop

#OPPURE
dati = dati %>% mutate(
  first_amount_spent = replace(first_amount_spent, first_amount_spent==0,fam)
) #ho sostuito le osservazioni zero con la mediana

dati = dati %>% mutate(
  number_of_products = replace(number_of_products, number_of_products==0,nop))
  

# Se variabile presenta valore 0 ed è da eliminare, la pongo come NA e poi elimino gli NA

table(dati$number_of_products)  # se è 0 elimino quella riga perchè dato mancante

dati$number_of_products[dati$number_of_products==0] <- NA

# Oppure se è vuoto il campo, pongo NA e poi elimino NA
dati$COUNTRY[dati$COUNTRY==""] <- NA
```

### Spazi vuoti

```R
na_spazio_vuoto = function(dati){
  na_list = sapply(dati, function(x) which(x == ""))
  ritorno = data.frame(variabile = names(na_list))
  for(i in 1:length(na_list)){
    ritorno$n_na[i] = length(na_list[[i]])
    ritorno$proporzione[i] = length(na_list[[i]])/dim(dati)[1]
  }
  return(ritorno)
}
na.obs2 = na_spazio_vuoto(dati)
na.obs2

options(max.print = nrow(dati))#per far vedere tutte le righe con valori vuoti

#ci sono?
sum(na.obs2$n_na>0)
#quali sono?
var_sp_vuoti=na.obs2[na.obs2$n_na>0,00001]

# 1) se sono casuali metto NA
for(variabile in var_sp_vuoti) {
  # Applichiamo l'assegnazione desiderata
  dati[[variabile]][dati[[variabile]] == ""] <- NA #o " "
}

# 2) SE CI SONO SPAZI VUOTI CON UN SIGNIFICATO PER ALCUNE VARIABILI DICOTOMICHE 

# VARIABILI CON PIU DI 2 MODALITA
vars_con_piu_di_due_modalita <- sapply(dati[var_sp_vuoti], function(x) nlevels(x) > 2)

# Nomi delle variabili che soddisfano il criterio
nomi_vars_con_piu_di_due_modalita <- names(vars_con_piu_di_due_modalita[vars_con_piu_di_due_modalita])

for (variabile in nomi_vars_con_piu_di_due_modalita) {
  # Converti la variabile in carattere
  dati[[variabile]] <- as.character(dati[[variabile]])
  
  # Sostituisci le stringhe vuote con "0"
  dati[[variabile]][dati[[variabile]] == " "] <- NA
  
  # Sostituisci le stringhe "X" con "1"
  # dati[[variabile]][dati[[variabile]] == "X"] <- "1"
  
  # Converti di nuovo in fattore
  dati[[variabile]] <- as.factor(dati[[variabile]])
}

# VARIABILI CON 2 MODALITA 
vars_con_due_modalita <- sapply(dati[var_sp_vuoti], function(x) nlevels(x) ==2)

# Nomi delle variabili che soddisfano il criterio
nomi_vars_con_due_modalita <- names(vars_con_due_modalita[vars_con_due_modalita])


for (variabile in nomi_vars_con_due_modalita) {
  # Converti la variabile in carattere
  dati[[variabile]] <- as.character(dati[[variabile]])
  
  # Sostituisci le stringhe vuote con "0"
  dati[[variabile]][dati[[variabile]] == " "] <- "0"
  
  # Sostituisci le stringhe "X" con "1"
   dati[[variabile]][dati[[variabile]] == "X"] <- "1"
  
  # Converti di nuovo in fattore
  dati[[variabile]] <- as.factor(dati[[variabile]])
}
```

## Sistemazione Variabili

### Tipologia delle variabili
```R
# identifico variabili numeriche e categoriali iniziali
num <-  unlist(lapply(dati, is.numeric))
numeriche <- which(num)
categoriali <-  which(!num)


# indico i valori unici per le variabili quantitative
n_unici <-  apply(dati[,names(numeriche)],2,function(x) length(unique(x)))
n_unici


# elimino le variabili costanti
var_costanti=names(which(n_unici==1))
var_canc=c("imp.sini.co",".n.dis.co", var_costanti) # tutte le variabili che voglio eliminare
dati=dati%>%select(-all_of(var_canc))


# eventuali variabili da ricodificare come fattori se val. unici < 4
fnum.tclass <- names(numeriche[which(n_unici < 4)]) 
dati[,fnum.tclass] <- lapply(dati[,fnum.tclass], function(x) factor(x))


# trovo i valori unici per le variabili fattoriali e ricodifico in variabili quantitative se val. unici > 4
for (col in 1:length(categoriali)) {
  valori_unici <- unique(dati[,names(categoriali)[col]])
  
  if (length(valori_unici) > 4) {
	dati[,names(categoriali)[col]] <- as.integer(dati[,names(categoriali)[col]])
  }
}


# mi salvo quelle che vado a considerare come variabili quantitative
numeriche <-  numeriche[which(n_unici >= 4)]


# identifico le eventuali nuove variabili numeriche 
num <- unlist(lapply(dati, is.numeric))
numeriche <- which(num)


# identifico le eventuali nuove variabili categoriali 
cat <- unlist(lapply(dati, is.factor))
categoriali <- which(cat)


# mi salvo solo le variabili con almeno due modalita
categoriali <-  categoriali[which(n.unici >= 2)]


# ricodifico il dataset con le variabili scelte
dati <- dati %>% select(all_of(c(categoriali,numeriche)))


## Cambiare la tipologia di tante variabili in fattore
## visualizzo le classi di ogni variabile
sapply(dati,class)
# ciclo for per convertire le variabili chr in factor
for(i in names(dati)[sapply(dati, is.character)]){
	dati[[i]]=factor(dati[[i]])
}
# ciclo for per convertire le variabili da char a numeric
for(i in names(dati)[sapply(dati, is.character)]){
	dati[[i]]=as.numeric(dati[[i]])
}

# prendo tutte le variabili con ".1" nel nome e le trasformo in fattori
lista <- grep(".1", names(dati))
dati[lista] <- lapply(dati[lista], as.factor)
str(dati)
```

### Tipologia delle variabili (B)
```R
str(dati)
tipo_variabile = sapply(dati %>% select(-y), class)
table(tipo_variabile)
# se ci sono caratteri li converto
var_qualitative = names(dati %>% select(-y))[tipo_variabile =="factor"]
var_quantitative = setdiff(names(dati %>% select(-y)), var_qualitative)
var_qualitative
var_quantitative

#se voglio vedere quale variabile è di un certo tipo
#colonne_logiche <- sapply(dati, function(col) class(col) == "logical")
#nomi_colonne_logiche <- names(colonne_logiche[colonne_logiche])
#nomi_colonne_logiche

# Rendo factor tutte le variabili character:
var_char = names(dati %>% select(-y))[tipo_variabile == "character"]
for(col in var_char) dati[,col] = as.factor(dati[,col])

# Rendo numeric tutte le v. quantitative che sono int:
var_int = names(dati %>% select(-y))[tipo_variabile == "integer"]
for(col in var_int) dati[,col] = as.numeric(dati[,col])

# Rendo factor tutte le logical:
var_log = names(dati %>% select(-y))[tipo_variabile == "logical"]
for(col in var_log) dati[,col] = as.factor(dati[,col])


#Aggiorno var qual e var quant
tipo_variabile = sapply(dati %>% select(-y), class)
var_qualitative = names(dati %>% select(-y))[tipo_variabile =="factor"]
var_quantitative = setdiff(names(dati %>% select(-y)), var_qualitative)
var_qualitative
var_quantitative
```

### Raggruppo per popolazione
```R
dati <- dati %>% 
    group_by(stato) %>% 
    mutate(
        cases_rate = cases/pop2020*1e+06, 
        mortality_rate = deaths/pop2020*1e+06
    )
dati <- as.data.frame(dati)
```

### Raggruppare modalità meno frequenti settandole in una modalità altro (A)
```R
summary(data.frame(table(dati$xbimbumbamx))) # TODO: Da includere?????

livelli <- c(levels(dati$comu),"altro") # Aggiungo il livello "altro" a quelli già presenti

dati$comu <- factor(dati$comu, levels=livelli)

str(attr(table(dati$comu), "dimnames")) # TOODO: Why?

table(dati$comu) # TOODO: Why? Solo per vedere la tabella di frequenza attuale?

comuni.piccoli <- names(which(table(dati$comu) <= 12))

dati$comu[dati$comu %in% comuni.piccoli ] <- "altro"

dati$comu <- factor(dati$comu)

table(dati$comu)
```

### Raggruppare modalità meno frequenti settandole in una modalità altro (B) 
```R
sort(table(dati$altreconsulenze)) # tante modalità vuote

cons = trimws(as.character(dati$altreconsulenze))

cons[cons==""] = "Non presente" # modalità per 'Non presente'

sort(table(cons))

rare = names(which(table(cons) < 10)) # nomi dei più rari

cons[cons %in% rare] = "Altro"

dati$altreconsulenze = as.factor(cons)
```

### Raggruppare una variabile in fasce d'età
```R
summary(dati$ANNO_NASCITA) # 1942    1968    1980    1979    1991    2002

dati$fasce_eta[dati$ANNO_NASCITA<=2002 & dati$ANNO_NASCITA >1992]="ragazzo"

dati$fasce_eta[dati$ANNO_NASCITA<=1992 & dati$ANNO_NASCITA >1965]="adulto"

dati$fasce_eta[dati$ANNO_NASCITA<=1965]="anziano"

dati$fasce_eta=factor(dati$fasce_eta)

dati$fasce_eta=ordered(dati$fasce_eta, levels=c("ragazzo", "adulto", "anziano"))

summary(dati$fasce_eta)

dati <- dati %>% select(-ANNO_NASCITA)
```

### Raggruppare le modalità di V2
```R
summary(dati$V2) 

dati$attività[dati$V2=="1" | dati$V2=="2" | dati$V2=="3" | dati$V2=="8"   | dati$V2=="9" ]="riposo"

dati$attività[dati$V2=="10" | dati$V2=="12" | dati$V2=="13" | dati$V2=="14"  | 

dati$V2=="15" | dati$V2=="16"]="attivita_moderata"

dati$attività[dati$V2=="4"]="camminata"

dati$attività[dati$V2=="5"]="corsa"

dati$attività[dati$V2=="6"]="bicicletta"

dati$attività[dati$V2=="7"]="Nordic walking"

dati$attività[dati$V2=="17"]="giocare a calcio"

# TODO: Qui non va messo fatto dati$attività=factor(dati$attività) ???

table(dati$attività)
```

### Raggruppare due modalità della variabile qualitativa
```R
dati <- dati %>% mutate(
    nome_variabile = recode_factor(
        nome_variabile, "modalità_da_elimin" = "mod_a_cui_accorpare"
    )
)
```

### Raggruppare le modalità in base alla data
```R
library(lubridate)

# Formatto la data con yyyy-mm-dd 
dati$data <-as.Date(dati$data, format="%Y-%m-%d")

dati$Anno = year(dati$data)

table(dati$Anno)

dati$Mese = month(dati$data)

table(dati$Mese)

dati$data = NULL
```

#### Raggruppare per mese, giorno, se festivo o meno
```R
library(lubridate)

dati$mese=month(dati$accesso)

dati$mese=factor(dati$mese)

dati$fest=ifelse(wday(dati$accesso, week_start=1)>=6, "Festivo", "Feriale")

dati$fest=factor(dati$fest)

# per semplicità trasformo 'accesso' in numerica
dati$accesso=as.numeric(dati$accesso)
```

#### Raggruppare per stagioni (A)
```R
table(dati$Mese)

dati= dati %>%
    mutate(
        Mese = as.numeric(as.character(Mese)), # Converti Mese in numerico
        Mese = case_when(
            Mese >= 9 & Mese <= 11 ~ "autunno",
            Mese >= 12 & Mese <= 2 ~ "inverno", 
            Mese >= 3 & Mese <= 5 ~ "primavera",
            Mese >= 6 & Mese <= 8 ~ "estate" # Cambiato a 8 per includere agosto in estate
        )
    )

dati$Mese=as.factor(dati$Mese)
```

#### Raggruppare per stagioni (B - con variabile mese fattoriale)
```R
dati$month = ifelse(dati$month %in% c( "mar" ,"apr","may"), "Primavera", 
                   ifelse(dati$month %in% c("nov","oct", "sep"), "Autunno", 
                          ifelse(dati$month %in% c("jun","jul", "aug"), "Estate", "Inverno")))


dati$month=as.factor(dati$month)
```

#### Raggruppare per momento della giornata
```R
momento <- rep(0, nrow(dati))

momento[orario %in% 6:12] = 'mattina'

momento[orario %in% 13:18] = 'pomeriggio'

momento[orario %in% 19:23] = 'sera'

momento[orario %in% 0:5] = 'notte'
```

#### Raggruppare per fascia oraria
```R
# Ci sono delimitatori sbagliati ("."): li correggo in ":"
ora1<- gsub("\\.", ":", dati$orario) 

# Devo selezionare solo l'ora e tralascio i minuti
ora2<- sub(":.*", "", ora1)

# Se avessi voluto selezionare minuti invece di secondi 
ora2<- sub("*.:", "", ora1)

# Ci sono due valori sbagliati controllo a che orario appartengono e decido che sono digitati male
ora1[ora2=="139"]
ora1[ora2=="15l"]

# Sostituisco
ora2[ora2=="139"]=13 # penso che il 9 sia stato digitato per sbaglio
ora2[ora2=="15l"]=15

gsub(as.Date(dati$ora_GMT), "", dati$ora_GMT)

dati <- dati %>%
  mutate(
    ora = as.character(ora), # Convert factor to character
    ora = as.numeric(substr(ora, 1, 2)), # Extract hour part
    ora= case_when(
     ora >= 6 &ora  < 12 ~ "mattino",
     ora  >= 12 & ora  < 18 ~ "pomeriggio",
     ora  >= 18 & ora  < 24 ~ "sera",
     ora  >= 0 & ora  < 6 ~ "notte"
    )
  )
dati$ora=as.factor(dati$ora)
table(dati$ora)
```


## Variabile Risposta y

### Non avere valori negativi
```R 
summary(dati$y) # non ci sono valori inferiori a zero?

dati=dati[dati$y>0,]
```

### Per la classificazione, rinomino 0 e 1  
```R
dati$y <- recode_factor(dati$y, "XXXX" = "0", .default = "1")
dati$y <- ifelse(dati$y== "female", "1", "0")
dati$y = as.factor(dati$y)

dati$y[dati$diagnosi == "affette"] = 1
dati$y[dati$diagnosi == "altro"] = 0
table(dati$y)
```

### Se risposta con tante modalità, raggruppo le modalità della risposta qui ricodificato le modalita della risposta (qualità) da 7 a 3 utilizzando i terzili
```R
dati$y <- cut(dati$y,
          breaks = quantile(dati$y, 0:3/3, na.rm = T),
          include.lowest = T)

table(dati$y)
```

### Trasformazioni variabili 
```R
dati$v1 = factor(dati$v1)
dati$v1=as.numeric(dati$v1) #traforma la variabile in numerica
dati$v1=as.character(dati$v1) #traforma fattore in variabile di testo
dati$v1=as.factor(dati$v1) #trasforma la variabile in fattore
```

### (Queste cose non servono ma meglio lasciarle)
```R
split <- sample(1:nrow(dati), 0.7*nrow(dati))
stima <- dati[split,]
verifica <- dati[-split,]

# Controllo che in verifica non ci siano modalità non presenti in stima
for(col in var_qualitative){
  if(!(all(unique(dati[dati$dataset == "verifica", col]) %in%
           unique(dati[dati$dataset == "stima", col])))){
    cat(col,"-> in verifica ci sono modalità non presenti in stima\n")
  }
}
```


### Correlazioni (Queste cose non servono ma meglio lasciarle)
```R
matr_corr = cor(dati %>% filter(dataset == "stima") %>% select(-c(var_qualitative, y, dataset)))
matr_corr[lower.tri(matr_corr, diag = T)] = NA
correlazioni = data.frame(var1 = rep(colnames(matr_corr), ncol(matr_corr)),
                          var2 = rep(rownames(matr_corr), each = nrow(matr_corr)),
                          cor = as.vector(matr_corr))
correlazioni = na.omit(correlazioni)
correlazioni %>% filter(cor < -0.95 | cor > 0.95)
dati <- dati %>% select(-var.troppo.correlata)
```

### Almeno che due x non siano correlate ad 1 non hai evidenza per togliere nessuna delle due
```R
# Standardizzo esplicative quantitative in stima e verifica
tipo_var = sapply(stima %>% dplyr::select(-y), class)
var_qualitative = names(stima %>% dplyr::select(-y))[tipo_var =="factor"]
var_quantitative = setdiff(names(stima %>% dplyr::select(-y)), var_qualitative)
stima[,var_quantitative] <- stima %>% dplyr::select(var_quantitative) %>% scale

tipo_var = sapply(verifica %>% dplyr::select(-y), class)
var_qualitative = names(verifica %>% dplyr::select(-y))[tipo_var =="factor"]
var_quantitative = setdiff(names(verifica %>% dplyr::select(-y)), var_qualitative)
verifica[,var_quantitative] <- verifica %>% dplyr::select(var_quantitative) %>% scale

varnames = colnames(dati %>% select(-c(y, dataset)))
varnames2 = colnames(xmat)
form.allvars = paste("y ~", paste(varnames,collapse = " + "), collapse = NULL)
```

### salvo documento molto grande
```R
write.csv(copurchase_archi, "copurchase_archi.csv",row.names = FALSE)
```


## Osservazioni ripetute dipendenti
```R
# Mi salvo l'identificativo del soggetto 
dati$id=as.factor(dati$id)


# IMPORTANTE SALVARE SU UN VETTORE ID
# SE PRESENTE UNA RIPETIZIONE TEMPORALE DEVI AGGIUNGERE COLONNA oss_per_sogg
dati <- as.data.frame(dati %>%
  group_by(Id_Cliente) %>% #puoi anche specificare piu var di ragg
                           #group_by(x1,x2) 
  mutate(oss_per_sogg = row_number()))

identificativo=dati$id

dati$id=NULL

## 1) Creazione del dataset per misure ripetute ##############
# - Per semplicità computazionale fingiamo che ciascun osservazione venga da un cliente diverso e quindi sia indipendente
# Infatti si nota che la maggior parte dei clienti hanno osservazioni ripetute ma non in egual modo, risultando in una difficoltà di divisione in stima e verifica se si volesse tener  conto di questa caratteristica
# Si nota come nel campione siano presenti più osservazioni per lo stesso soggetto, tuttavia è plausibile, in tal caso, ipotizzare indipendenza tra queste osservazioni perché       , quindi si decide di eliminare l’identificativo del soggetto e di procedere assumendo indipendenza tra tutte le osservazioni del campione. 

# - Dal momento che sono presenti piu osservazioni della stessa persona che sono dipendenti e si vuole mantenere una struttura di indipendenza tra le osservazioni, si campiona una osservazione per ogni soggetto avendo nell'insieme di verifica dei soggetti diversi da quelli che ci sono nell'insieme di stima, cosa che non accadrebbe se si facesse un campione casuale semplice

#stima-verifica nel caso di campionamento dei soggetti (oss ripetute)  piu di due per soggetto 
# Verifico che ci sia lo stesso numero di osservazioni per ciscun ID
table(dati$subject_id)
n <- nrow(dati)
n_unique <- length(unique(dati$subject_id)) #numero di soggetti totali 

rep_min = min(table(dati$subject_id)) # uguale o diverso da 1

##### Se osservazioni ripetute numero diseguale (=!1)
# Se n sufficientemente grandi e n_unique non troppo piccolo, ricampiono per ciascun soggetto il minimo delle osservazioni ripetute per soggetto rilevate
dati_save= dati   #Salvo dati originari

# Per ciascun ID, campiono casualmente rep_min osservazioni
library(tidyverse)
dati = dati %>% group_by(subject_id) %>%  sample_n(rep_min, replace=FALSE)
n = nrow(dati)
# Se rep_min   diverso da 1, passa 
# al codice successivo dove le osservazioni dello stesso
# soggetto vengono spostate sulla stessa riga

#dati <- dati%>% ungroup() #se necessario nel caso rep_min=1

#### Se osservazioni ripetute uguali (): ----
ind1 <- seq(1, n, by=rep_min)
ind2 <- ind1+1
ind3 <- ind1+2

# Ordino dati secondo ID
arrange(dati, id)
dati=arrange(dati, id)

# Creazione dei tre dataframe (tengo y=class nel primo dataset)
#class=c("gender", "class")
R1 <- dati[ind1,]
R2 <- dati[ind2,] %>% select(-class) #levo vari risposta o altre che dipendono da oss ripetute come gender
R3 <- dati[ind3,] %>% select(-class)

newname_R2 = c()
newname_R3 = c()

# Cambia nomi di R2 e R3
for (i in colnames(R2)){
  newname_R2 = append(newname_R2, paste(i, "R2"))
  newname_R3 = append(newname_R3, paste(i, "R3"))
}

colnames(R2) = newname_R2
colnames(R3) = newname_R3

R <- cbind(R1,R2, R3)

# Find identical columns
identical_columns <- which(duplicated(t(R))==TRUE)

# Print the identical columns
print(identical_columns)
var_identical = names(identical_columns)

# Salviamo dati
dati_save=dati 
dati = R %>% select(-var_identical)
row.names(dati)=NULL

# Necessario se le variabili contengono spazi
library(janitor)
dati <- as.data.frame(clean_names(dati)) 

# Elimino alla fine id:
dati$ID = NULL
str(dati)


# OPPURE QUANDO IPOTIZZO INDIPENDENZA SUDDIVIDO IN BASE ALL'IDENTIFICATIVO IN MODO CHE OSS DELLO STESSO SOGG STIANO INSIEME

set.seed(1234)
#stima/verifica per id:
stima_id <- sample(unique(dati$Id), round(length(unique(dati$Id))*0.75))
stima1 <- which(dati$Id%in% stima_id)
length(stima1)/nrow(dati) #nella stima ci stanno il --% delle osservazioni
stima=dati[stima1,]

verifica= setdiff(dati, stima)


#stima/convalida per id:
cb1_id <- sample(stima_id, round(length(stima_id)*0.7))
cb1 <- which(dati$Id %in% cb1_id)
length(cb1)/nrow(stima) #nella cb1 ci stanno il --% delle osservazioni

cb2 <- setdiff(stima1, cb1)



# Classificazione con 2 osservazioni--- bohhh
#stima e verifica  (come in occhi)
noss=2                      #noss è numero di osservazioni che ho sullo stesso id
dati$ID=c(1:NROW(dati))
dati0 <- dati[dati$y==0,]
ind0 <- factor(dati0$Paziente)
dati_prova0 <- dati0 %>% arrange(ind0)

dati1 <- dati[dati$y==1,]
ind1 <- (factor(dati1$Paziente))
dati_prova1 <- dati1 %>% arrange(ind1)

indici0=sample(dati_prova0$Paziente, NROW(dati0)*0.7)
indici1=sample(dati_prova1$Paziente, NROW(dati1)*0.7)

stima=rbind(dati0[indici0,], dati1[indici1,])
verifica=setdiff(dati,stima)

#divido in cb1 e cb2
cb1=c(sample(stima$ID[stima$y==0],length(indici0)*0.7), sample(stima$ID[stima$y==1],length(indici0)*0.7))
cb2=setdiff(stima$ID,cb1)
```









## Convalida Incrociata
```R
# Misurazioni ripetute con stessa frequenza

# 1) 
# Misurazioni ripetute sullo stesso paziente,
# suddivido tra pazienti con y=0 in un dataset e con y =1 in un altro
# ordino ID del paziente (comune anche senza repeated data, 
# non necessario ordinare per ID)

noss = 2 # noss è numero di osservazioni che ho sullo stesso id
dati0 <- dati[dati$y==0,]
ind0 <- factor(dati0$Paziente)
dati_prova0 <- dati0 %>% arrange(ind0)

dati1 <- dati[dati$y==1,]
ind1 <- (factor(dati1$Paziente))
dati_prova1 <- dati1 %>% arrange(ind1)


# 2) 
# Permutazione in coppia dei pazienti all'interno dei due dataset
# (nel caso senza repeated data, ricampiono solo 1)

set.seed(123)
enum0 <- rep(sample(length(ind0)), each=noss) 
dati_prova0 <- dati_prova0 %>% arrange(match(dati_prova0$Paziente,enum0))


dati0 <- dati_prova0

set.seed(345)
enum1 <- rep(sample(length(ind1)),, each=noss)
dati_prova1 <- dati_prova1 %>% arrange(match(dati_prova1$Paziente,enum1))

dati1 <- dati_prova1

#Permutazione dei Pazienti: I pazienti sono poi permutati all'interno di ogni gruppo #di y. Questo è fatto per assicurare che quando i dati sono divisi nei fold, i #pazienti siano distribuiti in modo casuale tra i fold. Si noti che set.seed è usato #per assicurare la riproducibilità del campionamento casuale.


# 3) Creazione dei k fold 
k <- 5
dati0$fold <- cut(seq(1:length(ind0)), breaks=k, labels=FALSE)
dati1$fold <- cut(seq(1:length(ind1)), breaks=k, labels=FALSE)

#Creazione dei Fold: I dati sono divisi in k fold (in questo caso, k=5) per ogni #gruppo di y. La funzione cut è usata per assegnare ogni osservazione a un fold.

# 4) Dataset finale contenente i dati assegnati casualmente, in coppie, 
dati <- rbind(dati0, dati1)
```

## Utiliy Commands

### Apply
```R
# Supponiamo di avere una matrice
matrice <- matrix(1:9, nrow = 3)

# Il 2 indica che vogliamo applicare la funzione alle colonne
apply(matrice, 2, sum) 

# Questo restituirà la somma di ogni colonna della matrice.
```

### Lapply
```R
# Supponiamo di avere una lista di numeri
lista <- list(a = 1:3, b = 4:6, c = 7:9)

lapply(lista, function(x) x^2)

#Questo restituirà una lista dove ogni elemento è il quadrato
```

### Sapply
```R
# Supponiamo di avere una lista di numeri
sapply(lista, function(x) x^2)

#ma desiderando un vettore o una matrice come output
```




### Scomporre il CAP
```R
# Prima cifra indica la macroregione
# Seconda cifra la provincia
# Terza cifra indica se città (1) o paese (0)
dati$regione = floor(dati$cap / 10000) # Prima cifra e basta

dati$regione = factor(dati$regione)

dati$citta = factor((dati$cap %% 1000) >= 100)

# Infine elimino il CAP
dati$cap = NULL
```

### Differenza tra due date
```R
dati$durata = (as.Date(dati$Data.termine) - as.Date(dati$Data.inizio))

dati$durata = as.numeric(dati$durata)
```

#### Creo un vettore di pesi da usare nell'analisi
```R
ww = dati.s$b1_1
```
