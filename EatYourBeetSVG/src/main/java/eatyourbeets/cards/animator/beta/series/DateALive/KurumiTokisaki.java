package eatyourbeets.cards.animator.beta.series.DateALive;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.DieDieDieEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.modifiers.CostModifiers;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class KurumiTokisaki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KurumiTokisaki.class).SetAttack(3, CardRarity.RARE, EYBAttackType.Ranged, EYBCardTarget.ALL).SetSeriesFromClassPackage();

    public KurumiTokisaki()
    {
        super(DATA);

        Initialize(12, 12, 2);
        SetUpgrade(0,0,0);
        SetAffinity_Orange(2, 0, 0);
        SetAffinity_Dark(1, 0, 0);

        SetAffinityRequirement(Affinity.Dark, 3);
        SetAutoplay(true);
        SetEthereal(true);

        SetCooldown(3, 0, this::OnCooldownCompleted);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
        SetAffinityRequirement(Affinity.Dark, 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.SFX("ATTACK_HEAVY");
        GameActions.Bottom.VFX(new DieDieDieEffect());
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.GUNSHOT);
        if (CheckAffinity(Affinity.Dark)) {
            GameActions.Bottom.StackPower(new EnergizedPower(p, magicNumber));
        }
        GameActions.Bottom.SelectFromHand(name, 999, false)
                .SetOptions(true, true, true)
                .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[0])
                .SetFilter(c -> c instanceof AnimatorCard && ((AnimatorCard) c).series != null && !c.hasTag(DELAYED))
                .AddCallback(cards ->
                {
                    for (AbstractCard card : cards) {
                        GameActions.Bottom.ModifyTag(card,DELAYED,true);
                    }
                });
        cooldown.ProgressCooldownAndTrigger(m);
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        GameActions.Bottom.VFX(new BorderFlashEffect(Color.RED, true));

        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.SelectFromHand(name, 1, true)
                    .SetFilter(GameUtilities::CanPlayTwice)
                    .AddCallback(cards ->
                    {
                        final String key = cardID + uuid;
                        for (AbstractCard card : cards) {
                            GameActions.Bottom.MakeCardInDrawPile(card).SetUpgrade(upgraded,true).AddCallback(c -> {
                                CostModifiers.For(card).Add(key, -1);
                                GameActions.Bottom.ModifyTag(c,AUTOPLAY,true);
                                GameActions.Bottom.ModifyTag(c,PURGE,true);
                            });
                        }
                    });
        }
    }
}