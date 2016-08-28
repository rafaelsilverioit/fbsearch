# FacebookSearch 

## Description

FacebookSearch module is part of a Early Warning System called GT-EWS which focus on prevent and detect security threats.

The main achievement of GT-EWS in its first stage (November, 2014 - October, 2015) was to delivery a prototype of an Early Warning System (EWS) that anticipates security events and incidents against network and computer systems located at RNP (Brazilian NREN).

We built a prototype composed by a web interface, message collectors, network sensors and web services. Our first results a faster detection than if the prototype was not used.

Briefly, it was confirmed that Facebook and Twitter are relevant sources of information for an EWS to anticipate alerts about data leaks, attack orchestrations and defacements of web pages on RNP network.

## Hardware requirements

Minium recommendations:

* RAM: 512 MB;
* Hard drive: 50 GiB HD;
* Nobreak;
* Internet access.

## Dependencies

The dependencies for FacebookSearch are the following:

* Java Runtime Environment 8 (JRE 8);
* PostgreSQL 9.1;
* IDE Eclipse for Java Developers - Download;
* Linux OS (such as CentOS, Debian, Ubuntu or Mint).

And the following libraries (already included):

* c3p0-0.9.5-pre10.jar;
* commons-lang3-3.4.jar;
* commons-codec-1.9.jar;
* commons-logging-1.2.jar;
* fluent-hc-4.4.jar;
* httpclient-4.4.jar;
* httpclient-cache-4.4.jar;
* httpclient-win-4.4.jar;
* httpcore-4.4.jar;
* httpmime-4.4.jar;
* jna-4.1.0.jar;
* jna-platform-4.1.0.jar;
* json-simple-1.1.1.jar;
* longurlJavaAPI.jar;
* mchange-commons-java-0.2.8.jar;
* postgresql-9.4-1200.jdbc4.jar;
* restfb-1.11.0.jar;
* stringsearch-2.1.jar.

## Database

FacebookSearch stores data on a PostgreSQL database, so you just need to restore <b>./database/fb_posts.dump</b> on a PostgreSQL server.

## Running

In order to run this module, run FBSearch.jar from a console as below:

  <pre>$ java -jar FBSearch.jar</pre>
  
Once started, everything will be stored in fb_posts database and posts_security.json. The output is used to integrate FacebookSearch with the central EWS system.

## Documentation and media

Official EWS website:

https://gtews.ime.usp.br/ <br />
https://horus.ime.usp.br/ <br />
https://horus.rnp.br/#/

Official EWS documentation:

Screenshots and video demonstrations: https://horus.ime.usp.br/demo.php <br />
RNP (Brazil's NREN) - GT-EWS description: https://www.rnp.br/sites/default/files/gt-ews.pdf

Scientific papers:

TNC'16 - GT-EWS presentation: https://tnc16.geant.org/core/presentation/707 <br />
TNC'16 - GT-EWS recorded presentation: https://tnc16.geant.org/web/media/archive/2B <br />
TNC'16 - GT-EWS paper: https://tnc16.geant.org/getfile/2691 <br />
TNC'16 - GT-EWS slides: https://tnc16.geant.org/getfile/2762

WRNP 2015 - Workshop RNP (Brazil's NREN): https://www.dropbox.com/s/o7zi0ok6pe9qt4a/WRNP2015_Lamina_Stand_03.pdf?dl=0 <br />
WRNP 2016 - Workshop RNP (Brazil's NREN): http://wrnp.rnp.br/sites/wrnp2016/files/wrnp2016_lamina_gt_ews.pdf

General media websites:

IDG NOW!: http://idgnow.com.br/internet/2016/08/18/novo-sistema-criado-na-usp-protege-comunidade-cientifica-de-ataques/ <br />
Brasileiros: http://brasileiros.com.br/MFlIE <br />
ComputerWorld: http://computerworld.com.br/pesquisador-da-usp-cria-sistema-contra-ataques-comunidade-cientifica

## Authors

Authors of this module are:

Rafael Silvério Amaral - rafael.silverio.it@gmail.com <br />
Rodrigo Campiolo - rcampiolo@utfpr.edu.br
