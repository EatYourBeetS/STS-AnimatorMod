package eatyourbeets.cards.animator.beta.TouhouProject;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class YuukaKazami extends AnimatorCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final EYBCardData DATA = Register(YuukaKazami.class).SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Elemental);

    public YuukaKazami()
    {
        super(DATA);

        Initialize(8, 0, 1, 0);
        SetUpgrade(3, 0, 0, 0);
        SetScaling(1, 0, 1);

        SetSpellcaster();
        SetSynergy(Synergies.TouhouProject);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(2);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.POISON);
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.POISON);
        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.ChannelOrb(new Earth(), true);
        }
    }

    @Override
    public void triggerOnManualDiscard()
    {
        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.ChannelOrb(new Earth(), true);
        }
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[0] + GameActionManager.turn + cardData.Strings.EXTENDED_DESCRIPTION[1], true);
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[0] + GameActionManager.turn + cardData.Strings.EXTENDED_DESCRIPTION[1], true);
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        return super.cardPlayable(m) && !isInAutoplay && (GameActionManager.turn % 2 == 0);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        CombatStats.onStartOfTurnPostDraw.Subscribe(this);
    }
}

