package eatyourbeets.cards.animator.curse;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_Curse;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.vfx.megacritCopy.HemokinesisEffect2;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Curse_Parasite extends AnimatorCard_Curse implements StartupCard
{
    public static final EYBCardData DATA = Register(Curse_Parasite.class)
            .SetCurse(-2, EYBCardTarget.None, false);

    public Curse_Parasite()
    {
        super(DATA, true);

        Initialize(0, 0, 2, 2);

        SetUnplayable(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();
        if (CombatStats.TryActivateSemiLimited(cardID)) {
            GameActions.Bottom.DealDamageToRandomEnemy(secondaryValue, DamageInfo.DamageType.HP_LOSS, AttackEffects.NONE)
                    .SetDamageEffect(enemy ->
                    {
                        GameEffects.List.Add(new HemokinesisEffect2(enemy.hb.cX, enemy.hb.cY, player.hb.cX, player.hb.cY));
                        return 0f;
                    });
            GameActions.Bottom.GainTemporaryHP(secondaryValue);
        }
    }

    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
    }

    @Override
    public boolean atBattleStartPreDraw() {
        GameActions.Bottom.DealDamageAtEndOfTurn(player, player, magicNumber, AttackEffects.POISON);
        return true;
    }
}