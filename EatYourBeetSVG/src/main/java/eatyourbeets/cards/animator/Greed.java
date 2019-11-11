package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MalleablePower;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.rewards.SpecialGoldReward;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Greed extends AnimatorCard
{
    public static final String ID = Register(Greed.class.getSimpleName(), EYBCardBadge.Exhaust, EYBCardBadge.Special);

    public Greed()
    {
        super(ID, 4, CardType.POWER, CardRarity.RARE, CardTarget.SELF);

        Initialize(0,0, 2, 8);

        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (GetMasterDeckInstance() != null && EffectHistory.TryActivateLimited(cardID))
        {
            AbstractRoom room = PlayerStatistics.GetCurrentRoom();
            if (room != null && room.rewardAllowed)
            {
                room.rewards.add(0, new SpecialGoldReward(cardData.strings.NAME, secondaryValue));
            }
        }
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        int discount = Math.floorDiv(AbstractDungeon.player.gold, 100);
        if (this.costForTurn > 0 && !this.freeToPlayOnce)
        {
            this.modifyCostForTurn(-discount);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.ApplyPower(p, p, new PlatedArmorPower(p, magicNumber), magicNumber);
        GameActionsHelper.ApplyPower(p, p, new MetallicizePower(p, magicNumber), magicNumber);
        GameActionsHelper.ApplyPower(p, p, new MalleablePower(p, magicNumber), magicNumber);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }
}