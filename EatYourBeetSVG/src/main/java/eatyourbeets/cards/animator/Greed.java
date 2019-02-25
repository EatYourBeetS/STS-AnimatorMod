package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MalleablePower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.subscribers.OnBattleEndSubscriber;

public class Greed extends AnimatorCard implements OnBattleEndSubscriber
{
    public static final String ID = CreateFullID(Greed.class.getSimpleName());

    public Greed()
    {
        super(ID, 2, CardType.POWER, CardRarity.RARE, CardTarget.SELF);

        Initialize(0,0, 3);

        AddExtendedDescription();

        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.ApplyPower(p, p, new PlatedArmorPower(p, this.magicNumber), this.magicNumber);
        GameActionsHelper.ApplyPower(p, p, new MalleablePower(p, this.magicNumber), this.magicNumber);

        for (OnBattleEndSubscriber s : PlayerStatistics.onBattleEnd.GetSubscribers())
        {
            if (s instanceof Greed)
            {
                return;
            }
        }
        PlayerStatistics.onBattleEnd.Subscribe(this);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }

    @Override
    public void OnBattleEnd()
    {
        int gold = AbstractDungeon.player.maxHealth - AbstractDungeon.player.currentHealth;
        if (gold > 0)
        {
            AbstractRoom room = PlayerStatistics.GetCurrentRoom();
            if (room != null && room.rewardAllowed)
            {
                room.addGoldToRewards(gold);
            }
        }
    }
}