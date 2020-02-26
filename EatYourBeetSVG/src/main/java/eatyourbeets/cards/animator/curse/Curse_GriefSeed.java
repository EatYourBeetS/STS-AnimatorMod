package eatyourbeets.cards.animator.curse;

import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.base.AnimatorCard_Curse;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Curse_GriefSeed extends AnimatorCard_Curse {
    public static final EYBCardData DATA = Register(Curse_GriefSeed.class).SetCurse(-2, EYBCardTarget.None);

    public Curse_GriefSeed()
    {
        super(DATA);

        Initialize(0, 0, 0);
        SetExhaust(true);

        SetSynergy(Synergies.MadokaMagica);
    }

    @Override
    public void triggerOnExhaust()
    {
        GameActions.Bottom.Callback(__ -> {
            AbstractPlayer player = AbstractDungeon.player;
            for (int i = player.powers.size() - 1; i >= 0; i--)
            {
                AbstractPower power = player.powers.get(i);
                if (power.type == AbstractPower.PowerType.DEBUFF)
                {
                    GameActions.Bottom.RemovePower(player, player, power);
                    return;
                }
            }
        });
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {

    }
}