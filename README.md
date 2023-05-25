#Beschreibung für ein Genetischen Algorithmus für das Traveling Salesman Problem

Es gibt verschiedene Modifikationen für das TSP:
- Datensatz:
  - die "Strecke" die der Algorithmus lösen soll. Es gibt in '/Datensätze' verschieden datensätze.
  - wichtig ist das man den ZOOM anpasst (siehe unten)
  - Man kann auch zufällige Punkte erstellen
- startingPoints:
  - Anzahl der Punkte die zu Beginn berücksichtigt werden sollen
  - TSP.BEGINNWITHALLPOINTS -> Alle Punkte
  - TSP.BEGINNWITHATENTH -> 1/10 der Punkte
- STARTPOP:
  - Anzahl der zu Beginn initialisierten Permutationen
  - nur sinnvoll, wenn die Voroptimierung an ist 
- ANZAHL_SAVINGS
  - Anzahl der Permutationen die für die nächste Iteration behalten werden
- CHILD_NUMBER
  - Anzahl der generierten "Kinder" einer Permutation
- VOROPTIMIERUNG
    - Zu Beginn wird ein Greedy-Algorithmus auf den datensatz angewendet


Man sollte um alle Punkte sehen zu können Main.ZOOM anpassen:
- berlin52:  2.0
- usa48   :  8.1
- pr1002  : 19
- ch130   : 1.1
- zufall  : 1.0

Um zufällige Punkte zu erhalten, muss man den Kommentar in Main anschalten.

