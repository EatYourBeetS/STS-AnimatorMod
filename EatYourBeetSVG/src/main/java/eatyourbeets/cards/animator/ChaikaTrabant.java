package eatyourbeets.cards.animator;

import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.subscribers.OnStartOfTurnPostDrawSubscriber;

public class ChaikaTrabant extends AnimatorCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final String ID = CreateFullID(ChaikaTrabant.class.getSimpleName());

    private int timer;

    public ChaikaTrabant()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);

        Initialize(7,0, 4);

        AddExtendedDescription();

        this.purgeOnUse = true;
        SetSynergy(Synergies.Chaika);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));

        if (m.intent != AbstractMonster.Intent.STUN && !m.hasPower(StunMonsterPower.POWER_ID))
        {
            GameActionsHelper.AddToBottom(new StunMonsterAction(m, p));
        }

        timer = baseMagicNumber;
        this.purgeOnUse = true;

        PlayerStatistics.onStartOfTurnPostDraw.Subscribe(this);
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(7);
        }
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        AbstractPlayer p = AbstractDungeon.player;
        if (timer == 0)
        {
            timer = baseMagicNumber;

            GameActionsHelper.AddToBottom(new MakeTempCardInDrawPileAction(this, 1, true, true));
            PlayerStatistics.onStartOfTurnPostDraw.Unsubscribe(this);
        }
        else
        {
            timer -= 1;
        }
    }
}