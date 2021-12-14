package eatyourbeets.cards.animator.series.Konosuba;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.events.shrines.Transmogrifier;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Vanir extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Vanir.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Elemental)
            .SetSeriesFromClassPackage();

    public Vanir()
    {
        super(DATA);

        Initialize(12, 0, 3);
        SetUpgrade(1, 0, -1);

        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Dark(1, 0, 0);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.SelectFromPile(name, 1, player.drawPile)
        .SetOptions(false, true)
        .SetMessage(Transmogrifier.OPTIONS[2])
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                GameActions.Top.ReplaceCard(cards.get(0).uuid, makeCopy()).SetUpgrade(upgraded);
            }
        });
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (damage > 8)
        {
            GameActions.Bottom.SFX(SFX.ATTACK_DEFECT_BEAM);
            GameActions.Bottom.VFX(VFX.SmallLaser(p.hb, m.hb, Color.RED));
            GameActions.Bottom.DealCardDamage(this, m, AttackEffects.FIRE).forEach(d -> d
            .SetDamageEffect(c -> GameEffects.List.Add(VFX.SmallExplosion(c.hb)).duration * 0.1f));
        }
        else
        {
            GameActions.Bottom.Wait(0.25f);
            GameActions.Bottom.DealCardDamage(this, m, AttackEffects.SMASH);
        }
        GameActions.Bottom.ModifyAllInstances(uuid, c -> c.baseDamage = Math.max(0, c.baseDamage - c.magicNumber));
    }
}