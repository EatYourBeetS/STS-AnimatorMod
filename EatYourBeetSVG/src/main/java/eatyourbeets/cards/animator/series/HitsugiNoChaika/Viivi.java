package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DaggerSprayEffect;
import eatyourbeets.cards.animator.special.ThrowingKnife;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.utilities.GameActions;

public class Viivi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Viivi.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Ranged, EYBCardTarget.Random);
    static
    {
        DATA.InitializePreview(ThrowingKnife.GetCardForPreview(), false);
    }

    public Viivi()
    {
        super(DATA);

        Initialize(3, 0, 3);
        SetUpgrade(0, 0, 1);
        SetScaling(0, 1, 0);

        SetSynergy(Synergies.Chaika);
    }

    @Override
    protected void OnUpgrade()
    {
        upgradedDamage = true;
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.CreateThrowingKnives(1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.VFX(new DaggerSprayEffect(AbstractDungeon.getMonsters().shouldFlipVfx()), 0.0F);
            GameActions.Bottom.DealDamageToRandomEnemy(this, AbstractGameAction.AttackEffect.NONE);
        }

        GameActions.Bottom.Draw(1);
//        GameActions.Bottom.SelectFromHand(name, 1, true)
//        .SetFilter(c -> c.type == CardType.ATTACK && !c.retain && !c.selfRetain)
//        .AddCallback(cards ->
//        {
//            if (cards.size() > 0)
//            {
//                cards.get(0).retain = true;
//                cards.get(0).flash();
//            }
//        });
    }
}