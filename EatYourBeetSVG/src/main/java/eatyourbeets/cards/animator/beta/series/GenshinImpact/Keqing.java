package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.vfx.combat.DieDieDieEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.TargetHelper;

public class Keqing extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Keqing.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Piercing).SetSeriesFromClassPackage();

    public Keqing()
    {
        super(DATA);

        Initialize(2, 0, 3);
        SetUpgrade(1, 0, 1);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Green(2, 0, 2);
        SetAffinity_Dark(0, 0, 1);


        SetRicochet(4, 0, this::OnCooldownCompleted);

        SetExhaust(true);
        SetHitCount(3);
    }

    @Override
    public void triggerWhenDrawn() {
        if (this.hasTag(HASTE)) {
            GameActions.Bottom.ChannelOrb(new Lightning());
            GameActions.Bottom.ApplyElectrified(TargetHelper.Enemies(), magicNumber);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.DAGGER).forEach(d -> d.SetDamageEffect(c -> GameEffects.List.Add(new DieDieDieEffect()).duration));
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        GameActions.Bottom.MoveCard(this, player.exhaustPile, player.drawPile)
                .ShowEffect(true, true);
        GameActions.Bottom.ModifyTag(this, HASTE, true);
    }

}