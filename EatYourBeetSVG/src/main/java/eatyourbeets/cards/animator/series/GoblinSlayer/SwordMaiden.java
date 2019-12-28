package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;

public class SwordMaiden extends AnimatorCard implements StartupCard
{
    public static final String ID = Register(SwordMaiden.class, EYBCardBadge.Special);

    public SwordMaiden()
    {
        super(ID, 2, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);

        Initialize(0, 0, 8);

        SetExhaust(true);
        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainTemporaryHP(this.magicNumber);
        GameActions.Bottom.Callback(__ ->
        {
            AbstractPlayer player = AbstractDungeon.player;
            for (int i = player.powers.size() - 1; i >= 0; i--)
            {
                AbstractPower power = player.powers.get(i);
                if (power.type == AbstractPower.PowerType.DEBUFF)
                {
                    GameActions.Bottom.RemovePower(player, player, power);
                    break;
                }
            }
        });
    }

    @Override
    public boolean atBattleStartPreDraw()
    {
        if (EffectHistory.TryActivateLimited(cardID))
        {
            GameActions.Bottom.GainRandomStat(2);

            return true;
        }

        return false;
    }
}