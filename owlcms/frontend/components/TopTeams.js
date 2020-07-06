import {PolymerElement, html} from '@polymer/polymer/polymer-element.js';
class TopTeams extends PolymerElement {
	static get is() {
		return 'topteams-template'
	}

	static get template() {
		return html`<style>
* {
	box-sizing: border-box;
}

/* shared default sizes */
:root {
	--narrow-width: 4.5vw;
	--max-narrow-width: 5ch;
	--veryNarrow-width: 4vw;
	--max-veryNarrow-width: 4ch;
	--medium-width: 6.5vw;
	--max-medium-width: 7ch;
	--category-width: 8ch;
	--max-category-width: 100%;
}

/* wide screen */
@media screen and (min-width: 1401px) {
	.wideTeams {
		--fontSizeRank-height: 1.1em;
		--fontSizeRows-height: 1.1em;
		--narrow-width: 5vw;
		--max-narrow-width: 100%;
		--veryNarrow-width: 4vw;
		--max-veryNarrow-width: 100%;
		--medium-width: 5.5vw;
		--max-medium-width: 100%;
		--needed-width: 8vw;
		--max-needed-width: 100%;
		--name-width: 20vw;
		--name-max-width: 100%;
		--club-width: 20vw;
		--club-max-width: 100%;
		--category-width: 12ch;
		--rank-width: 5ch;
	}
	.narrowTeams {
		--fontSizeRank-height: 1.1em;
		--fontSizeRows-height: 1.2em;
		--narrow-width: 6vw;
		--max-narrow-width: 100%;
		--veryNarrow-width: 6vw;
		--max-veryNarrow-width: 100%;
		--medium-width: 8vw;
		--max-medium-width: 100%;
		--needed-width: 8vw;
		--max-needed-width: 100%;
		--name-width: 20vw;
		--name-max-width: 100%;
		--club-width: 8vw;
		--club-max-width: 8ch;
		--category-width: 12ch;
		--rank-width: 5ch;
	}
	.showThRank {
		border-collapse: collapse;
		border: solid 1px DarkGray;
		border-left-style: none;
		padding: 0.5vmin 1vmin 0.5vmin 1vmin;
		font-size: var(--fontSizeRank-height);
		font-weight: normal;
		font-style: italic;
		width: 4vw;
		text-align: center;
		font-size: var(--fontSizeRank-height);
	}
	.showRank {
		display: table-cell;
		font-size: var(--fontSizeRows-height);
		text-align: center;
		width: var(--rank-width);
	}
	.showRank div {
		width: var(--rank-width);
		margin: auto;
	}
	th,td {
		font-size: var(--fontSizeRows-height);
	}
}

/* 720 screen or 1366 laptop */
@media screen and (max-width: 1400px) and (min-width: 1279px) {
	.wideTeams {
		--fontSizeRank-height: 0.90em;
		--fontSizeRows-height: 0.90em;
		--narrow-width: 5vw;
		--max-narrow-width: 100%;
		--veryNarrow-width: 4vw;
		--max-veryNarrow-width: 100%;
		--medium-width: 5.5vw;
		--max-medium-width: 100%;
		--needed-width: 8vw;
		--max-needed-width: 100%;
		--name-width: 20vw;
		--name-max-width: 100%;
		--club-width: 20vw;
		--club-max-width: 100%;
		--category-width: 12ch;
		--rank-width: 5ch;
	}
	.narrowTeams {
		--fontSizeRank-height: 1.1em;
		--fontSizeRows-height: 1.2em;
		--narrow-width: 6vw;
		--max-narrow-width: 100%;
		--veryNarrow-width: 6vw;
		--max-veryNarrow-width: 100%;
		--medium-width: 9vw;
		--max-medium-width: 100%;
		--needed-width: 8vw;
		--max-needed-width: 100%;
		--name-width: 20vw;
		--name-max-width: 100%;
		--club-width: 8vw;
		--club-max-width: 8ch;
		--category-width: 12ch;
		--rank-width: 5ch;
	}
	.showThRank {
		display: none;
		width: 0px;
		padding: 0 0 0 0;
		margin: 0 0 0 0;
		font-size: var(--fontSizeRank-height);
	}
	.showRank {
		display: none;
		width: 0px;
		padding: 0 0 0 0;
		margin: 0 0 0 0;
		font-size: var(--fontSizeRows-height);
	}
	th,td {
		font-size: var(--fontSizeRows-height);
	}
}

/* 1024 projector */
@media screen  and (max-width: 1024px) {
	.wideTeams {
		--fontSizeRank-height: 0.75em;
		--fontSizeRows-height: 0.75em;
		--narrow-width: 5vw;
		--max-narrow-width: 100%;
		--veryNarrow-width: 6ch;
		--max-veryNarrow-width: 100%;
		--medium-width: 6vw;
		--max-medium-width: 100%;
		--needed-width: 8vw;
		--max-needed-width: 100%;
		--name-width: 16vw;
		--name-max-width: 100%;
		--club-width: 16vw;
		--club-max-width: 100%;
		--category-width: 12ch;
		--rank-width: 5ch;
	}
	.narrowTeams {
	  --fontSizeRank-height: 0.8em;
	  --fontSizeRows-height: 0.9em;
	  --narrow-width: 6vw;
	  --max-narrow-width: 100%;
	  --veryNarrow-width: 6vw;
	  --max-veryNarrow-width: 100%;
	  --medium-width: 8vw;
	  --max-medium-width: 100%;
	  --needed-width: 8vw;
	  --max-needed-width: 100%;
      --name-width: 20vw;
	  --name-max-width: 100%;
	  --club-width: 8vw;
	  --club-max-width: 8ch;
	  --category-width: 12ch;
	  --rank-width: 5ch;
	}
	/* hide the snatch and cj ranks, no room */
	.showThRank {
		display: none;
		width: 0px;
		padding: 0 0 0 0;
		margin: 0 0 0 0;
		font-size: var(--fontSizeRank-height);
	}
	/* hide the snatch and cj ranks, no room */
	.showRank {
		display: none;
		width: 0px;
		padding: 0 0 0 0;
		margin: 0 0 0 0;
		font-size: var(--fontSizeRows-height);
	}
	th,td {
		font-size: var(--fontSizeRows-height);
	}
}

.wrapper {
	font-family: Arial, Helvetica, sans-serif;
	color: white;
	background-color: black;
	min-height: 100vh;
	padding: 2vmin 2vmin 2vmin 2vmin;
	overflow: hidden;
	display: flex;
	flex-direction: column;
	flex-wrap: no-wrap;
	justify-content: flex-start;
}

#results {
	flex: 1 0 auto;
}

#leaders {
	flex: 0 0 auto;
	width: 100%;
	min-height: 0;
	align-self: flex-end;
	background-color: blue;
}

.attemptBar {
	display: flex;
	font-size: 3.6vmin;
	justify-content: space-between;
	width: 100%;
	height: 4vmin;
}

.attemptBar .startNumber {
	align-self: center;
}

.attemptBar .startNumber span {
	font-size: 70%;
	font-weight: bold;
	border-width: 0.2ex;
	border-style: solid;
	border-color: red;
	width: 1.5em;
	display: flex;
	justify-content: center;
	align-self: center;
}

.attemptBar .athleteInfo {
	display: flex;
	font-size: 3.6vmin;
	justify-content: space-between;
	align-items: baseline;
	width: 100%;
}

.athleteInfo .fullName {
	font-weight: bold;
	flex: 0 0 35%;
	text-align: left;
	flex-grow: 0.5;
}

.athleteInfo .timer {
	flex: 0 0 15%;
	text-align: right;
	font-weight: bold;
	width: 10vw;
	display: flex;
	justify-content: flex-end;
}

.athleteInfo .decisionBox {
	position: fixed;
	top: 2vmin;
	right: 2vmin;
	width: 15vw;
	height: 10vh;
	background-color: black;
	display: none;
}

.athleteInfo .weight {
	color: aqua;
	display: flex;
	justify-content: center;
	align-items: baseline;
}

.group {
	font-size: 3vh;
	margin-top: 1vh;
	margin-bottom: 2vh;
}

table.results {
    table-layout: fixed;
	width: 100%;
	border-collapse: collapse;
	border: none;
	background-color: black;
	/*margin-bottom: 2vmin;*/
}

:host(.dark) table.results tr {
	background-color: black;
	color: white;
}

:host(.light) table.results tr {
	background-color: white;
	color: black;
}

th, td {
	border-collapse: collapse;
	border: solid 1px DarkGray;
	padding: 0.4vmin 1vmin 0.4vmin 1vmin;
	font-size: var(--fontSizeRows-height);
}

:host(.dark) th, td {
	font-weight: normal;
}

:host(.light) th, td {
	font-weight: bold;
}

.name {
	width: var(--name-width);
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

.name div {
	max-width: calc(var(--name-max-width));
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}

.club {
	width: var(--club-width);
	text-align: center;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}

.club div {
	max-width: var(--club-max-width);
	text-align: center;
	margin: auto;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}

.ellipsis {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.thRank {
	border-collapse: collapse;
	border: solid 1px DarkGray;
	border-left-style: none;
	padding: 0.5vmin 1vmin 0.5vmin 1vmin;
	font-weight: normal;
	font-style: italic;
	width: var(--rank-width);
	text-align: center;
	font-size: var(--fontSizeRank-height);
}
.thRank div{
	width: var(--rank-width);
	margin: auto;
}

.narrow {
	width: var(--narrow-width);
	text-align: center;
}
.narrow div {
	width: var(--max-narrow-width);
	text-align: center;
	margin: auto;
}

.veryNarrow {
	width: var(--veryNarrow-width);
	text-align: center;
}
.veryNarrow div {
	width: var(--max-veryNarrow-width);
	text-align: center;
	margin: auto;
}

.medium {
	width: var(--medium-width);
}
.medium div {
	width: var(--max-medium-width);
	text-align: center;
	margin: auto;
}

.needed {
	width: var(--needed-width);
}
.needed div {
	width: var(--max-needed-width);
	text-align: center;
	margin: auto;
}

.groupCol {
	width: var(--group-width);
	white-space: nowrap;
	text-align: center;
	font-size: var(--fontSizeRank-height)
}
.groupCol div {
	width: var(--group-width);
	display: inline-block;
}

.category {
	width: var(--category-width);
	white-space: nowrap;
	text-align: center;
}

.category div {
	width: var(--category-width);
	display: inline-block;
}

:host(.dark) .good {
	background-color: green;
	font-weight: bold;
}

:host(.light) .good {
	background-color: green;
	font-weight: bold;
	color: white;
}

:host(.dark) .fail {
	background-color: red;
	font-weight: bold;
}

:host(.light) .fail {
	background-color: red;
	font-weight: bold;
	color: white;
}

:host(.dark)  .spacer {
	background-color: black;
}


:host(.light)  .spacer {
	background-color: gray;
}

.english {
	font-size: 85%;
}

:host(.dark) .request {
	background-color: black;
	font-style: italic;
}

:host(.light) .request {
	background-color: white;
	font-style: italic;
}

:host(.dark) .current {
	color: yellow;
	font-weight: bold;
}

:host(.light) .current {
	background-color: yellow;
	font-weight: bold;
}

.blink {
	animation: blink 1.5s step-start 0s infinite;
	-webkit-animation: blink 1.5s step-start 0s infinite;
}
@keyframes blink {
 50% {opacity: 0.0;}
}
@-webkit-keyframes blink {
 50% {opacity: 0.0;}
}


:host(.dark) .next {
	color: orange;
	font-weight: bold;
}

:host(.light) .next {
	background-color: gold;
	font-weight: bold;
}

:host(.dark) .empty {
	background-color: black;
	font-style: italic;
}

:host(.light) .empty {
	background-color: white;
	font-style: italic;
}

.breakTime {
	/* color: #99CCFF; */
	color: SkyBlue;
}

.athleteTimer {
	color: yellow;
}

.v-system-error {
	display: none;
}


table#leaders-table thead tr.hide {
	visibility: hidden; height:1px; line-height: 1px; font-size:1px;
}
table#leaders-table thead tr.hide th {
	visibility: hidden; height:1px; line-height: 1px; font-size:1px; padding:0;
}

.teams {
	font-weight: bold;
}

h2 {
  font-size: 3.0vh;
}
</style>
<div class$="wrapper [[_computeTeamWidth(wideTeamNames)]]" id="resultBoardDiv">
	<template is="dom-if" if="[[topTeamsWomen]]">
		<h2 class="fullName" id="fullNameDiv" inner-h-t-m-l="[[topTeamsWomen]]"></h2>
		<table class="results" id="orderDiv" style$="">
			<thead>
				<tr>
					<th class="club" inner-h-t-m-l="[[t.Team]]"></th>
					<th class="medium" inner-h-t-m-l="[[t.Done]]"></th>
					<th class="medium" inner-h-t-m-l="[[t.TeamSize]]"></th>
					<th class="medium"  inner-h-t-m-l="[[t.Points]]"></th>
				</tr>
			</thead>
			<template is="dom-repeat" id="result-table" items="[[womensTeams]]" as="l">
				<tr>
					<td class="club"><div>[[l.team]]</div></td>
					<td class="medium"><div>[[l.counted]]</div></td>	
					<td class="medium"><div>[[l.size]]</div></td>
					<td class="medium"><div>[[l.points]]</div></td>
				</tr>
			</template>
		</table>
		<h2>&nbsp;</h2>
	</template>
	<template is="dom-if" if="[[topTeamsMen]]">
		<h2 class="fullName" id="fullNameDiv" inner-h-t-m-l="[[topTeamsMen]]"></h2>
		<table class="results" id="orderDiv" style$="">
			<thead>
				<tr>
					<th class="club" inner-h-t-m-l="[[t.Team]]"></th>
					<th class="medium" inner-h-t-m-l="[[t.Done]]"></th>
					<th class="medium" inner-h-t-m-l="[[t.TeamSize]]"></th>
					<th class="medium"  inner-h-t-m-l="[[t.Points]]"></th>
				</tr>
			</thead>
			<template is="dom-repeat" id="result-table" items="[[mensTeams]]" as="l">
				<tr>
					<td class="club"><div>[[l.team]]</div></td>
					<td class="medium"><div>[[l.counted]]</div></td>	
					<td class="medium"><div>[[l.size]]</div></td>
					<td class="medium"><div>[[l.points]]</div></td>
				</tr>
			</template>
		</table>
		<h2>&nbsp;</h2>
	</template>
</div>`;
	}

	ready() {
		super.ready();
		this.$.resultBoardDiv.style.display="block";
	}

	start() {
		this.$.resultBoardDiv.style.display="block";
	}

	reset() {
		console.debug("reset");
		this.$.resultBoardDiv.style.display="block";
	}

	down() {
		console.debug("down");
	}

	doBreak() {
		console.debug("break");
		this.$.resultBoardDiv.style.display="block";
	}

	groupDone() {
		console.debug("done");
		this.$.resultBoardDiv.style.display="block";
	}

	refereeDecision() {
		console.debug("refereeDecision");
	}

	_isEqualTo(title, string) {
		return title == string;
	}

	clear() {
		this.$.resultBoardDiv.style.display="none";
	}

	_computeTeamWidth(w) {
		return w ? 'wideTeams' : 'narrowTeams';
	}

}

customElements.define(TopTeams.is, TopTeams);
