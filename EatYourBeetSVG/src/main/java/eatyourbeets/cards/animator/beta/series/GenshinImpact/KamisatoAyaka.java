package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.vfx.combat.FallingIceEffect;
import eatyourbeets.cards.animator.beta.special.SheerCold;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.animator.NegateBlockPower;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class KamisatoAyaka extends AnimatorCard {
    public static final EYBCardData DATA = Register(KamisatoAyaka.class).SetAttack(2, CardRarity.RARE, EYBAttackType.Piercing, EYBCardTarget.Random).SetSeriesFromClassPackage();
    private static final int NO_BLOCK_TURNS = 2;
    static
    {
        DATA.AddPreview(new SheerCold(), false);
    }

    public KamisatoAyaka() {
        super(DATA);

        Initialize(7, 0, 1, 3);
        SetUpgrade(2, 0, 0, 0);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Green(2, 0, 1);

        SetExhaust(true);
    }

    @Override
    public void initializeDescription()
    {
        super.initializeDescription();

        if (cardText != null)
        {
            tooltips.add(GR.Tooltips.Chilled);
        }
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        GameUtilities.IncreaseMagicNumber(this, this.getFrostCount(), true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing) {

        for (int i = 0; i < magicNumber; i++)
        {
            GameEffects.Queue.Add(new FallingIceEffect(magicNumber,false));
            CardCrawlGame.sound.playA("ORB_FROST_Evoke", -0.25f - (float)magicNumber / 200f);
            GameActions.Bottom.DealDamageToRandomEnemy(this, AbstractGameAction.AttackEffect.NONE).SetOptions(true, false);
        }

        GameActions.Bottom.StackPower(new NegateBlockPower(p, NO_BLOCK_TURNS));

        if (this.getFrostCount() >= secondaryValue && CombatStats.TryActivateLimited(cardID))
        {
            AbstractCard c = new SheerCold();
            c.applyPowers();
            c.use(player, null);
        }
    }

    private int getFrostCount() {
        return JUtils.Count(player.orbs, orb -> Frost.ORB_ID.equals(orb.ID));
    }
}