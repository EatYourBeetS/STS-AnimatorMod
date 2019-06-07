package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.subscribers.OnStartOfTurnPostDrawSubscriber;

public class SilverFang extends AnimatorCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final String ID = CreateFullID(SilverFang.class.getSimpleName());

    public SilverFang()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.COMMON, CardTarget.SELF_AND_ENEMY);

        Initialize(8, 7, 2);

        SetSynergy(Synergies.OnePunchMan);
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp)
    {
        return super.calculateModifiedCardDamage(player, mo, tmp + PlayerStatistics.GetDexterity(player));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.GainBlock(p, this.block);
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.BLUNT_HEAVY);

        PlayerStatistics.onStartOfTurnPostDraw.Subscribe(this);
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(3);
            //upgradeBlock(1);
            upgradeMagicNumber(1);
        }
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        PlayerStatistics.onStartOfTurnPostDraw.Unsubscribe(this);

        AbstractPlayer p = AbstractDungeon.player;
        PlayerStatistics.ApplyTemporaryDexterity(p, p, magicNumber);
    }
}