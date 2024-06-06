
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/stat.h>
#include <unistd.h>
#include <pwd.h>
#include <dirent.h>
#include "config.h"
#include "qmailadmin.h"
#include "qmailadminx.h"
extern FILE *lang_fs;
extern FILE *color_table;
#define SORT_TABLE_ENTRIES 100000
unsigned char **sort_list;
unsigned char *sort_block[200];
int memleft, memindex, sort_entry;
char *sort_ptr;
int sort_init () {
sort_entry = memindex = memleft = 0;
sort_list = malloc(SORT_TABLE_ENTRIES * sizeof(char *));
if (!sort_list) { return -1; }
return 0; }
int sort_add_entry (char *entry, char end) {
int len;
if (sort_entry == SORT_TABLE_ENTRIES) {
return -2; }
if (memleft < 256) {
memleft = 65536;
sort_ptr = sort_block[memindex++] = malloc(memleft); }
if (!sort_ptr) { return -1; }
sort_list[sort_entry++] = sort_ptr;
len = 1;
while (*entry && *entry != end) {
*sort_ptr++ = *entry++;
len++; }
*sort_ptr++ = 0;
memleft -= len; }
char *sort_get_entry(int index) {
if ((index < 0) || (index >= sort_entry)) { return NULL; }
return sort_list[index]; }
void sort_cleanup() {
while (memindex) { free (sort_block[--memindex]); } }
static int sort_compare (const void *p1, const void *p2) {
return strcasecmp (*(char **)p1, *(char **)p2); }
void sort_dosort() {
qsort (sort_list, sort_entry, sizeof(char *), sort_compare); }
void str_replace (char *s, char orig, char repl) {
while (*s) {
if (*s == orig) { *s = repl; }
s++; } }
void qmail_button(char *modu, char *command, char *user, char *dom, time_t mytime, char *png) {
fprintf(actout, "<td align=center>");
fprintf(actout, "<a href=\"%s/com/%s?user=%s&dom=%s&time=%d&modu=%s\">",
CGIPATH, command, user, dom, mytime, modu);
fprintf(actout, "<img src=\"%s/%s\" border=0></a>", IMAGEURL, png);
fprintf(actout, "</td>\n"); }
check_local_user( user )
char *user; {
struct stat buf;
int i,j;
strcpy(TmpBuf, ".qmail-");
for(i=0,j=7;user[i]!=0;++i,++j){
if ( user[i] == '.' ) TmpBuf[j] = ':';
else TmpBuf[j] = user[i]; }
TmpBuf[j] = 0;
if ( stat(TmpBuf, &buf) == 0 ) return(-1);
if ( vauth_getpw(user, Domain)) return(-1);
return(0); }
show_counts() {
count_users();
count_forwards();
count_autoresponders();
count_mailinglists();
fprintf(actout, "%s = %d<BR>\n", get_html_text("061"), CurPopAccounts);
fprintf(actout, "%s = %d<BR>\n", get_html_text("074"), CurForwards);
fprintf(actout, "%s = %d<BR>\n", get_html_text("077"), CurAutoResponders);
fprintf(actout, "%s = %d<BR>\n", get_html_text("080"), CurMailingLists); }
check_email_addr( addr )
char *addr; {
char *taddr = addr;
if(strlen(taddr)<0) return(1);
for(taddr=addr;*taddr!=0;++taddr) {
if(!isalnum(*taddr) && !ispunct(*taddr)) {
return(1); } }
lowerit(addr);
for(taddr=addr;*taddr!='@'&&*taddr!=0;++taddr) {
if ( isspace(*taddr) ) return(1);
if(ispunct(*taddr) &&
*taddr!='.' && *taddr!='-' && *taddr!='+' && *taddr!='=' &&
*taddr!='_') {
return(1); } }
if (*taddr==0) return(1);
for(;*taddr!='.'&&*taddr!=0;++taddr);
if (*taddr==0) return(1);
return(0); }
fixup_local_name( addr )
char *addr; {
char *taddr = addr;
if(strlen(taddr)<=0) return(1);
lowerit(addr);
for(taddr=addr;*taddr!=0;++taddr) {
if(!isalnum(*taddr) && !ispunct(*taddr)) return(1);
if(isspace(*taddr)) return(1);
if(ispunct(*taddr)&&*taddr!='-'&&*taddr!='.'&&*taddr!='_' &&
*taddr!='+' && *taddr!='=') {
if(*taddr!='.') return(1); } }
return(0); }
ack(msg, c)
char *msg;
int c; {
fprintf(actout,"%s\n", msg);
fprintf(actout,"</BODY></HTML>\n", msg);
vclose();
exit(0); }
upperit( instr )
char *instr; {
while(*instr!=0) {
if ( islower(*instr) ) *instr = toupper(*instr);
++instr; } }
char *safe_getenv(char *var) {
char *s;
s = getenv(var);
if ( s == NULL ) return("");
return(s); }
char *strstart(sstr, tstr)
char *sstr;
char *tstr; {
char *ret_str;
ret_str = sstr;
if ( sstr == NULL || tstr == NULL ) return(NULL);
while ( *sstr != 0 && *tstr != 0 ) {
if ( *sstr != *tstr ) return(NULL);
++sstr;
++tstr; }
if ( *tstr == 0 ) return(ret_str);
return(NULL); }
int open_lang(char *lang) {
char langfile[200];
static char *langpath = NULL;
struct stat mystat;
if ( strstr(lang,".")!=NULL || strstr(lang,"/")!=NULL ) return(-1);
if (lang_fs != NULL) fclose (lang_fs);
if (langpath == NULL) {
langpath = getenv(QMAILADMIN_TEMPLATEDIR);
if (langpath == NULL ) langpath = HTMLLIBDIR; }
snprintf(langfile, sizeof(langfile), "%s/lang/%s", langpath, lang);
if (lstat(langfile, &mystat)==-1 || S_ISLNK(mystat.st_mode)) return(-1);
if ( (lang_fs=fopen(langfile, "r"))==NULL) return(-1);
return(0); }
char *get_html_text( char *index ) {
char *tmpbuf;
char *tmpstr;
tmpbuf = malloc(400);
if (lang_fs == NULL) return("");
rewind(lang_fs);
while(fgets(tmpbuf,400,lang_fs)!=NULL){
tmpstr = strtok(tmpbuf, " ");
if (strcmp(tmpstr, index) == 0 ) {
tmpstr = strtok(NULL, "\n");
return(tmpstr); } }
return(""); }
int open_colortable() {
char tmpbuf[200];
char *tmpstr;
tmpstr = getenv(QMAILADMIN_TEMPLATEDIR);
if (tmpstr == NULL ) tmpstr = HTMLLIBDIR;
snprintf(tmpbuf, sizeof(tmpbuf), "%s/html/colortable", tmpstr);
if ( (color_table=fopen(tmpbuf, "r"))==NULL) return(-1);
return(0); }
char *get_color_text( char *index ) {
char tmpbuf[400];
char *tmpstr;
if (color_table == NULL) return("");
rewind(color_table);
while(fgets(tmpbuf,sizeof(tmpbuf),color_table)!=NULL){
tmpstr = strtok(tmpbuf, " ");
if (strcmp(tmpstr, index) == 0 ) {
tmpstr = strtok(NULL, "\n");
return(tmpstr); } }
return(""); }
int quota_to_bytes(char returnval[], char *quota) {
char *tmpstr;
double tmp;
if (quota == NULL) { return 1; }
if (tmp = atof(quota)) {
tmp *= 1048576;
sprintf(returnval, "%.0lf", tmp);
return 0;
} else {
strcpy (returnval, "");
return 1; } }
int quota_to_megabytes(char *returnval, char *quota) {
char *tmpstr;
double tmp;
int i;
if (quota == NULL) { return 1; }
i = strlen(quota);
if ((quota[i-1] == 'M') || (quota[i-1] == 'm')) {
tmp = atol(quota);
} else if ((quota[i-1] == 'K') || (quota[i-1] == 'k')) {
tmp = atol(quota) * 1024;
} else if (tmp = atol(quota)) {
tmp /= 1048576.0;
} else {
strcpy (returnval, "");
return 1; }
sprintf(returnval, "%.2lf", tmp);
return 0; }