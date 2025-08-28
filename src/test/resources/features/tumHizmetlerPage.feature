Feature: uctan uca test
  Background: e devlet ana sayfa
    Given e devlete ana ekrana gidilir
    And arama cubuguna golbasi yazilir
    And golbasi belediyesi sayfasina gidilir
    Then tum hizmetler sekmesine gidilir

  Scenario: arsa metrekare mahalle adi isimler alfabetik mi ve tekrar eden var mi
    Given arsa metrekare ekranina girilir
    And mahalle verileri cekilir
    Then alfabetik mi ve tekrar eden var mi dogrulanir

  Scenario: arsa metrekare caddeSokak adi isimler alfabetik mi ve tekrar eden var mi
    Given arsa metrekare ekranına gidilir
    And ahiboz mahallesi secilir
    And cadde verileri cekilir
    Then alfabetik mi ve tekrar eden varmi dogrulanir

  Scenario: arsa metrekare yil listesi sirali mi
    Given arsa metrekare alanina gidilir
    And yil listesinin verileri cekilir
    Then buyukten kucuge mi siralanmis dogrulanir

  Scenario: hata mesajı cikiyor mu
    Given arsa metrekare alanına gidilir
    And sorgula butonuna basilir
    Then gerekli hata mesaji cikiyor mu dogrulanir

  Scenario: arsa rayic basliklari cikiyor mu
    Given arsa metrakare ekranina girilir
    When eymir mahallesi secilir
    And 3803. cadde secilir
    And 2025 yili secilir
    And sorgula butonuna tiklanir
    Then basliklar cikiyor mu dogrulanir

  Scenario: herhangi bir kayit bulunamadi yazisi cikiyor mu
    Then beyan bilgileri ekranına gidilir ve yazi kontrol edilir

  Scenario:
    Then sicil bilgileri ekranına gidilir ve yazi kontrol edilir

  Scenario:
    Then tahakkuk bilgileri ekranına gidilir ve yazi kontrol edilir

  Scenario:
    Then tahsilat bilgileri ekranına gidilir ve yazi kontrol edilir
