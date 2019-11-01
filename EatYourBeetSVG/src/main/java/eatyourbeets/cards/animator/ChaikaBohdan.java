package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.interfaces.OnBattleStartSubscriber;
import eatyourbeets.interfaces.OnAttackSubscriber;

public class ChaikaBohdan extends AnimatorCard implements OnBattleStartSubscriber, OnAttackSubscriber
{
    public static final String ID = Register(ChaikaBohdan.class.getSimpleName(), EYBCardBadge.Special);

    private int bonusDamage = 0;

    public ChaikaBohdan()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(5,0,2);

        if (PlayerStatistics.InBattle() && !CardCrawlGame.isPopupOpen)
        {
            OnBattleStart();
        }

        this.baseSecondaryValue = this.secondaryValue = 3;

        AddExtendedDescription();
        SetSynergy(Synergies.Chaika);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);

        int handSize = p.hand.size();
        if (p.hand.contains(this))
        {
            handSize -= 1;
        }
        if (handSize <= 0)
        {
            GameActionsHelper.DrawCard(p, this.secondaryValue);
        }

        AddDamageBonus(-bonusDamage);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeDamage(4);
        }
    }

    @Override
    public void OnBattleStart()
    {
        PlayerStatistics.onAttack.Subscribe(this);
    }

    @Override
    public void OnAttack(DamageInfo info, int damageAmount, AbstractCreature target)
    {
        AbstractPlayer player = AbstractDungeon.player;
        if (player.hand.contains(this) && target instanceof AbstractMonster && info.owner == player && info.type != DamageInfo.DamageType.THORNS)
        {
            for (AbstractCard c : GetAllInBattleInstances.get(this.uuid))
            {
                AddDamageBonus(this.magicNumber);
            }

            this.flash();
        }
    }

    private void AddDamageBonus(int amount)
    {
        bonusDamage += amount;
        baseDamage += amount;
    }
}