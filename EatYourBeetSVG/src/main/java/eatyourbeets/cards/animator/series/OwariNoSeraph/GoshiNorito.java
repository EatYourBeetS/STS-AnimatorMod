package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.stances.IntellectStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

import java.util.Iterator;

public class GoshiNorito extends AnimatorCard implements Hidden
{
    public static final EYBCardData DATA = Register(GoshiNorito.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None).SetSeriesFromClassPackage().PostInitialize((data) -> {
        data.AddPreview(AffinityToken.GetCard(Affinity.Red), false);
    });

    public GoshiNorito() {
        super(DATA);
        this.Initialize(0, 2, 2);
        this.SetUpgrade(0, 1, 0);
        this.SetAffinity_Red(1);
        this.SetAffinity_Blue(1, 1, 0);
        this.SetExhaust(true);
    }

    @Override
    public AbstractAttribute GetBlockInfo() {
        return this.magicNumber > 1 ? super.GetBlockInfo().AddMultiplier(this.magicNumber) : super.GetBlockInfo();
    }

    @Override
    public void OnDrag(AbstractMonster m) {
        super.OnDrag(m);

        for (EnemyIntent intent : GameUtilities.GetIntents()) {
            intent.AddWeak();
        }

    }


    @Override
    protected float GetInitialBlock() {
        int debuffs = 0;

        AbstractMonster m;
        for(Iterator<AbstractMonster> var2 = GameUtilities.GetEnemies(true).iterator(); var2.hasNext(); debuffs += GameUtilities.GetDebuffsCount(m)) {
            m = (AbstractMonster)var2.next();
        }

        return super.GetInitialBlock() + (float)debuffs;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        for(int i = 0; i < this.magicNumber; ++i) {
            GameActions.Bottom.GainBlock(this.block).SetVFX(i > 0, i > 0);
        }

        GameActions.Bottom.ApplyWeak(info.IsStarter ? p : null, p, 1);
        GameActions.Bottom.ApplyWeak(TargetHelper.Enemies(), 1);
        if (IntellectStance.IsActive()) {
            GameActions.Bottom.ObtainAffinityToken(Affinity.Red, false);
        }

    }
}