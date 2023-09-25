package eatyourbeets.cards.animator.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.series.MadokaMagica.NagisaMomoe;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class NagisaMomoe_CharlotteAlt extends AnimatorCard
{
    public static final EYBCardData DATA = Register(NagisaMomoe_CharlotteAlt.class)
            .SetAttack(2, CardRarity.SPECIAL, EYBAttackType.Normal)
            .SetSeries(NagisaMomoe.DATA.Series);

    public NagisaMomoe_CharlotteAlt()
    {
        super(DATA);

        Initialize(15, 0, 4);
        SetUpgrade(4, 0);

        SetAffinity_Dark(2, 0, 1);
        SetAffinity_Red(1, 1, 0);
        SetAffinity_Blue(1, 1, 0);

        SetRetain(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.BITE)
        .SetDamageEffect(e ->
        {
            GameEffects.List.BorderFlash(Color.RED);
            return GameEffects.List.Add(VFX.Hemokinesis(player.hb, e.hb)).duration * 0.1f;
        })
        .AddCallback(info, (info2, enemy) ->
        {
            if (GameUtilities.IsFatal(enemy, true))
            {
                if (info2.TryActivateLimited())
                {
                    GameActions.Top.Heal(magicNumber);
                }
                GameUtilities.ModifyCostForCombat(this, -1, true);
                GameActions.Delayed.MoveCard(this, player.hand)
                .ShowEffect(false, false);
            }
        });
    }
}