package pinacolada.cards.pcl.series.DateALive;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.DieDieDieEffect;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.modifiers.CostModifiers;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class KurumiTokisaki extends PCLCard
{
    public static final PCLCardData DATA = Register(KurumiTokisaki.class)
            .SetAttack(3, CardRarity.RARE, PCLAttackType.Ranged, eatyourbeets.cards.base.EYBCardTarget.ALL)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    public KurumiTokisaki()
    {
        super(DATA);

        Initialize(12, 12, 2);
        SetUpgrade(0,0,1);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Dark(1, 0, 2);
        SetAffinity_Silver(1, 0, 1);

        SetAutoplay(true);
        SetEthereal(true);

        SetCooldown(2, 0, this::OnCooldownCompleted);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.SFX("ATTACK_HEAVY");
        PCLActions.Bottom.VFX(new DieDieDieEffect());
        PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.GUNSHOT);

        PCLActions.Bottom.SelectFromHand(name, magicNumber, false)
                .SetOptions(true, true, true)
                .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[0])
                .SetFilter(c -> !c.hasTag(DELAYED))
                .AddCallback(cards ->
                {
                    for (AbstractCard card : cards) {
                        PCLActions.Bottom.ModifyTag(card,DELAYED,true);
                        PCLActions.Bottom.GainEnergyNextTurn(1);
                    }
                });
        cooldown.ProgressCooldownAndTrigger(m);
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        PCLActions.Bottom.VFX(new BorderFlashEffect(Color.RED, true));
        PCLActions.Bottom.SelectFromPile(name, 1, player.drawPile, player.discardPile, player.hand)
                .SetOptions(false,true)
                .SetFilter(c -> PCLGameUtilities.CanPlayTwice(c) && c.hasTag(DELAYED))
                .AddCallback(cards ->
                {
                    final String key = cardID + uuid;
                    for (AbstractCard card : cards) {
                        PCLActions.Bottom.MakeCardInDrawPile(card).SetUpgrade(card.upgraded,true).AddCallback(c -> {
                            CostModifiers.For(card).Add(key, -1);
                            PCLActions.Bottom.ModifyTag(c,AUTOPLAY,true);
                            //GameActions.Bottom.ModifyTag(c,PURGE,true);
                        });
                    }
                });
        PCLActions.Bottom.Purge(this);
    }
}